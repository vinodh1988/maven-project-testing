import argparse, requests, xml.etree.ElementTree as ET

def parse_testng(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    results = []
    for testcase in root.findall(".//test-method"):
        name = testcase.attrib['name']
        status = testcase.attrib['status']
        results.append({
            "name": name,
            "status": status  # map to TestRail statuses
        })
    return results

def push_results(url, user, password, project_id, run_name, results):
    # Create test run
    res = requests.post(
        f'{url}/index.php?/api/v2/add_run/{project_id}',
        auth=(user, password),
        json={"name": run_name, "include_all": True}
    )
    run = res.json()
    print("Status Code:", res.status_code)
    print("Response Body:", res.text)
    run_id = run["id"]

    # Add results
    for r in results:
        status_id = 1 if r["status"] == "PASS" else 5  # 1: Passed, 5: Failed
        requests.post(
            f'{url}/index.php?/api/v2/add_result_for_case/{run_id}/{r["name"]}',
            auth=(user, password),
            json={"status_id": status_id, "comment": "Automated via Jenkins"}
        )

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--results')
    parser.add_argument('--url')
    parser.add_argument('--user')
    parser.add_argument('--password')
    parser.add_argument('--project-id')
    parser.add_argument('--run-name')
    args = parser.parse_args()

    test_results = parse_testng(args.results)
    push_results(args.url, args.user, args.password, args.project_id, args.run_name, test_results)
