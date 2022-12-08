# Python Script inspiration drawn from source: https://pyppeteer.github.io/pyppeteer/
import asyncio
from pyppeteer import launch
import os

project_root = os.path.dirname(os.path.dirname(__file__))
generated_report_path = '/service/target/site'
custom_reports_path = '/service/reports'

async def screenshot_reports():
    browser = await launch()
    page = await browser.newPage()

    # Screenshot Checkstyle Report HTML Page
    await page.goto(f'{project_root}{generated_report_path}/checkstyle.html')
    await page.screenshot({
        'path': f'{project_root}{custom_reports_path}/checkstyle-report.png',
        'clip': {
            'x': 0,
            'y': 0,
            'width': 1000,
            'height': 500
        }
    })

    # Screenshot Jacoco Test Coverage Report HTML Page
    await page.goto(f'{project_root}{generated_report_path}/jacoco/index.html')
    await page.screenshot({
        'path': f'{project_root}{custom_reports_path}/test-coverage-report.png',
        'fullPage': 'true'
    })

    # Screenshot SpotBugs (Static Analysis Bug Finder Tool) Report HTML Page
    await page.goto(f'{project_root}{generated_report_path}/spotbugs.html')
    await page.screenshot({
        'path': f'{project_root}{custom_reports_path}/static-analysis-bug-finder-report.png',
        'clip': {
            'x': 0,
            'y': 0,
            'width': 1000,
            'height': 500
        }
    })
    
    await browser.close()

asyncio.get_event_loop().run_until_complete(screenshot_reports())
