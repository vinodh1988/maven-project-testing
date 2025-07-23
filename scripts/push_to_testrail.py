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

def parse_testng_results(xml_path):
    tree = ET.parse(xml_path)
    root = tree.getroot()
    results = []
    for test in root.iter("test-method"):
        name = test.attrib["name"]
        status = test.attrib["status"]
        case_id = None
        if "C" in name:
            try:
                case_id = int(name.split("C")[1].split("_")[0])
            except:
                pass
        if case_id:
            results.append({
                "case_id": case_id,
                "status_id": 1 if status == "PASS" else 5 if status == "FAIL" else 2,
                "comment": f"Automated result: {status}"
            })
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
    for suite in suites:
        print(f"- {suite}",suite)
    suite_id = suites[0]["id"]

    run_id = create_test_run(args.url, auth, args.project_id, suite_id, args.run_name)
    results = parse_testng_results(args.results)
    upload_results(results, args.url, auth, run_id)
