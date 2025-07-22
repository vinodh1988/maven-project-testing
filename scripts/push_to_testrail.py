#!/usr/bin/env python3
"""
push_to_testrail.py
~~~~~~~~~~~~~~~~~~~
Upload TestNG results to TestRail.

Assumptions
-----------
• Your TestNG test method names (or <test-method> names in the XML) contain the
  TestRail case ID with a capital 'C', e.g.  testCheckout_C1234.
  Feel free to adjust the `extract_case_id()` helper if you use a different scheme.
• You have API access enabled in TestRail and an API key (recommended) or password.
"""

import argparse
import sys
import re
import xml.etree.ElementTree as ET
from typing import Dict, List, Optional

import requests
requests.packages.urllib3.disable_warnings()  # silence self‑signed HTTPS warnings


# ---------- Helpers --------------------------------------------------------- #

def api_get(url: str, user: str, pwd: str) -> dict:
    r = requests.get(url, auth=(user, pwd), verify=False)
    if r.status_code != 200:
        sys.stderr.write(f"[ERROR] GET {url} -> {r.status_code} {r.text}\n")
        sys.exit(1)
    return r.json()


def api_post(url: str, user: str, pwd: str, payload: dict) -> dict:
    r = requests.post(url, auth=(user, pwd), json=payload, verify=False)
    if r.status_code >= 300:
        sys.stderr.write(f"[ERROR] POST {url} -> {r.status_code} {r.text}\n")
        sys.exit(1)
    return r.json()


def extract_case_id(method_name: str) -> Optional[int]:
    """
    Return the first integer after a capital 'C' in the method name, e.g. C1234.
    """
    m = re.search(r"C(\d+)", method_name)
    return int(m.group(1)) if m else None


def parse_testng(xml_path: str) -> List[Dict]:
    tree = ET.parse(xml_path)
    root = tree.getroot()
    results = []
    for tm in root.findall(".//test-method"):
        name = tm.attrib.get("name", "")
        status = tm.attrib.get("status", "").upper()
        cid = extract_case_id(name)
        if cid:
            results.append(
                {
                    "case_id": cid,
                    "status": status,  # PASS, FAIL, SKIP
                }
            )
    return results


def status_to_id(status: str) -> int:
    mapping = {
        "PASS": 1,   # Passed
        "FAIL": 5,   # Failed
        "SKIP": 2,   # Blocked (or use 4 = Retest, adjust to taste)
    }
    return mapping.get(status, 4)  # default to Retest


# ---------- Main flow ------------------------------------------------------- #

def upload_results(
    base_url: str,
    user: str,
    pwd: str,
    project_id: int,
    run_name: str,
    xml_path: str,
    suite_id_cli: Optional[int] = None,
) -> None:

    # 1️⃣  Determine suite mode
    proj = api_get(f"{base_url}/index.php?/api/v2/get_project/{project_id}", user, pwd)
    suite_mode = proj["suite_mode"]  # 1, 2, or 3

    if suite_mode in (1, 2):  # single‑suite project
        suite_id = None
    else:  # multi‑suite
        if suite_id_cli:
            suite_id = suite_id_cli
        else:
            suites = api_get(
                f"{base_url}/index.php?/api/v2/get_suites/{project_id}", user, pwd
            )
            if not suites:
                sys.stderr.write("[ERROR] No suites found in project.\n")
                sys.exit(1)
            suite_id = suites[0]["id"]
            print(f"[INFO] Using first suite: {suite_id} ({suites[0]['name']})")

    # 2️⃣  Create test run
    payload = {
        "name": run_name,
        "include_all": False,  # we will send specific case results
    }
    if suite_id:
        payload["suite_id"] = suite_id

    run = api_post(
        f"{base_url}/index.php?/api/v2/add_run/{project_id}", user, pwd, payload
    )
    run_id = run["id"]
    print(f"[INFO] Created run {run_id}")

    # 3️⃣  Parse TestNG XML
    results = parse_testng(xml_path)
    if not results:
        sys.stderr.write("[ERROR] No valid case IDs found in TestNG report.\n")
        sys.exit(1)

    # 4️⃣  Push each result
    for res in results:
        case_id = res["case_id"]
        status_id = status_to_id(res["status"])
        api_post(
            f"{base_url}/index.php?/api/v2/add_result_for_case/{run_id}/{case_id}",
            user,
            pwd,
            {"status_id": status_id, "comment": "Automated via Jenkins"},
        )
        print(f"  ↳ Case C{case_id}: {res['status']} (status_id={status_id})")

    # 5️⃣  (Optional) Close the run
    api_post(
        f"{base_url}/index.php?/api/v2/close_run/{run_id}", user, pwd, {}
    )
    print(f"[INFO] Closed run {run_id}")


# ---------- CLI ------------------------------------------------------------- #

if __name__ == "__main__":
    ap = argparse.ArgumentParser(description="Upload TestNG results to TestRail")
    ap.add_argument("--results", required=True, help="Path to testng-results.xml")
    ap.add_argument("--url", required=True, help="Base URL, e.g. https://<host>.testrail.io")
    ap.add_argument("--user", required=True, help="TestRail user/email")
    ap.add_argument("--password", required=True, help="TestRail API key or password")
    ap.add_argument("--project-id", required=True, type=int, help="Numeric Project ID")
    ap.add_argument("--run-name", required=True, help="Name for the new test run")
    ap.add_argument("--suite-id", type=int, help="Suite ID (optional for multi‑suite)")
    args = ap.parse_args()

    upload_results(
        base_url=args.url.rstrip("/"),
        user=args.user,
        pwd=args.password,
        project_id=args.project_id,
        run_name=args.run_name,
        xml_path=args.results,
        suite_id_cli=args.suite_id,
    )
