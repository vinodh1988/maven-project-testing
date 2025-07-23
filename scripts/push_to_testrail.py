import argparse
import requests
import xml.etree.ElementTree as ET
import sys

def get_suites(url, auth, project_id):
    res = requests.get(f'{url}/index.php?/api/v2/get_suites/{project_id}', auth=auth)
    if res.status_code != 200:
        print(f"❌ Failed to fetch suites. Status: {res.status_code}, Body: {res.text}")
        sys.exit(1)
    suites = res.json()
    if not suites:
        print("❌ No test suites found. Please add a test suite in TestRail.")
        sys.exit(1)
    return suites

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

import xml.etree.ElementTree as ET

def parse_testng_results(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    results = []

    for suite in root.findall("suite"):
        for test in suite.findall("test"):
            for class_elem in test.findall("class"):
                for method in class_elem.findall("test-method"):
					print(method)
                    name = method.attrib.get("name")
                    status = method.attrib.get("status")
                    
                    if name.startswith("c") and "_" in name:
                        try:
                            case_id = int(name.split("_")[0][1:])
                            status_id = 1 if status == "PASS" else 5
                            comment = f"Status: {status}"
                            results.append({
                                "case_id": case_id,
                                "status_id": status_id,
                                "comment": comment
                            })
                        except ValueError:
                            print(f"⚠️ Could not parse case_id from: {name}")
    return results


def upload_results(results, url, auth, run_id):
    res = requests.post(f'{url}/index.php?/api/v2/add_results_for_cases/{run_id}',
                        auth=auth,
                        json={"results": results})
    if res.status_code != 200:
        print(f"❌ Failed to upload results. Status: {res.status_code}, Body: {res.text}")
        sys.exit(1)
    print("✅ Results uploaded successfully.")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--results", required=True, help="Path to testng-results.xml")
    parser.add_argument("--url", required=True, help="TestRail URL")
    parser.add_argument("--user", required=True)
    parser.add_argument("--password", required=True)
    parser.add_argument("--project-id", required=True, type=int)
    parser.add_argument("--run-name", required=True)

    args = parser.parse_args()

    auth = (args.user, args.password)
    suites = get_suites(args.url, auth, args.project_id)
    print("Available test suites:")
    print(suites)
    for suite in suites:
        print(f"- {suite}",suite)
    suite_id = suites["suites"][0]["id"]

    run_id = create_test_run(args.url, auth, args.project_id, suite_id, args.run_name)
    print(args.results)
    results = parse_testng_results(args.results)
    print("Parsed results:", results)
    upload_results(results, args.url, auth, run_id)
