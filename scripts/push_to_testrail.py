import argparse
import requests
import xml.etree.ElementTree as ET
import sys
import os

def get_suites(url, auth, project_id):
    res = requests.get(f'{url}/index.php?/api/v2/get_suites/{project_id}', auth=auth)
    if res.status_code != 200:
        print(f"❌ Failed to fetch suites. Status: {res.status_code}, Body: {res.text}")
        sys.exit(1)
    return res.json()

def create_test_run(url, auth, project_id, suite_id, run_name):
    data = {
        "suite_id": suite_id,
        "name": run_name,
        "include_all": True
    }
    res = requests.post(f'{url}/index.php?/api/v2/add_run/{project_id}', auth=auth, json=data)
    if res.status_code != 200:
        print(f"❌ Failed to create test run. Status: {res.status_code}, Body: {res.text}")
        sys.exit(1)
    return res.json()["id"]

def parse_testng_results(xml_file):
    if not os.path.exists(xml_file):
        print(f"❌ XML file does not exist: {xml_file}")
        sys.exit(1)

    try:
        tree = ET.parse(xml_file)
        root = tree.getroot()
    except ET.ParseError as e:
        print(f"❌ Failed to parse XML: {e}")
        sys.exit(1)

    results = []

    for suite in root.findall(".//suite"):
        for test in suite.findall(".//test"):
            for class_elem in test.findall(".//class"):
                for method in class_elem.findall(".//test-method"):
                    name = method.attrib.get("name")
                    status = method.attrib.get("status")

                    if not name or not status:
                        continue

                    if name.startswith("C") and "_" in name:
                        try:
                            case_id = int(name.split("_")[0][1:])
                            status_id = 1 if status.upper() == "PASS" else 5
                            comment = f"Status: {status}"
                            results.append({
                                "case_id": case_id,
                                "status_id": status_id,
                                "comment": comment
                            })
                        except ValueError:
                            print(f"⚠️ Skipping invalid test case name: {name}")
    return results

def upload_results(results, url, auth, run_id):
    if not results:
        print("❌ No test results found to upload.")
        sys.exit(1)

    payload = {"results": results}
    res = requests.post(f'{url}/index.php?/api/v2/add_results_for_cases/{run_id}', auth=auth, json=payload)

    if res.status_code != 200:
        print(f"❌ Failed to upload results. Status: {res.status_code}, Body: {res.text}")
        sys.exit(1)
    print("✅ Results uploaded successfully.")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--results", required=True, help="Path to testng-results.xml")
    parser.add_argument("--url", required=True, help="TestRail base URL")
    parser.add_argument("--user", required=True, help="TestRail username/email")
    parser.add_argument("--password", required=True, help="TestRail API key/password")
    parser.add_argument("--project-id", required=True, type=int, help="TestRail project ID")
    parser.add_argument("--run-name", required=True, help="Name for the new test run")

    args = parser.parse_args()
    auth = (args.user, args.password)

    print("📡 Fetching test suites...")
    suites = get_suites(args.url, auth, args.project_id)

    if not suites or not suites.get("suites"):
        print("❌ No test suites found.")
        sys.exit(1)

    suite_id = suites["suites"][0]["id"]
    print(f"📂 Using Test Suite: {suite_id}")

    print("📝 Creating test run...")
    run_id = create_test_run(args.url, auth, args.project_id, suite_id, args.run_name)
    print(f"🆔 Test Run ID: {run_id}")

    print("📖 Parsing XML test results...")
    results = parse_testng_results(args.results)
    print(f"🔍 Parsed {len(results)} test results.")

    print("🚀 Uploading results to TestRail...")
    upload_results(results, args.url, auth, run_id)
