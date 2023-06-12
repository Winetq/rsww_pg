import json
import time
from bs4 import BeautifulSoup as Soup
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from templates import FlightTemplate

PAGE = "zaawansowana-wyszukiwarka-lotow"


class FlightScrapper:
    def __init__(self, URL, click_more_times=4):
        op = webdriver.ChromeOptions()
        op.add_argument('headless')
        delay = 10  # seconds
        sum = 0
        polishAirports = []
        airports = {}

        driver = webdriver.Chrome(options=op)
        url = URL + PAGE
        driver.get(url)

        try:
            WebDriverWait(driver, delay)\
                .until(EC.presence_of_element_located((By.CLASS_NAME, 'charters-destination-item__button-text')))
            airportNames = Soup(driver.page_source, 'lxml')\
                .find_all("p", class_="charters-destination-item__name")
            airportCountries = Soup(driver.page_source, 'lxml')\
                .find_all("p", class_="charters-destination-item__country")
            buttons = driver.find_elements(By.CLASS_NAME, "charters-destination-item__button-text")
            airportsNumber = len(buttons)

            for i in range(airportsNumber):
                if i > 0:
                    driver.get(url)
                    WebDriverWait(driver, delay)\
                        .until(EC.presence_of_element_located((By.CLASS_NAME, 'charters-destination-item__button-text')))
                    buttons = driver.find_elements(By.CLASS_NAME, "charters-destination-item__button-text")
                airports[airportNames[i].text.strip()] = airportCountries[i].text.strip()
                driver.execute_script("arguments[0].click();", buttons[i])
                WebDriverWait(driver, delay)\
                    .until(EC.presence_of_element_located((By.CLASS_NAME, 'charters-offers-list__load-more-wrapper')))

                #  load more flights
                for k in range(click_more_times):
                    buttonMore = Soup(driver.page_source, "lxml").find("div", "charters-offers-list__load-more-wrapper")
                    if buttonMore:
                        buttonMore = driver.find_element(By.CLASS_NAME, "charters-offers-list__load-more-wrapper")
                        buttonMore = buttonMore.find_element(By.CLASS_NAME, "button")
                        if buttonMore is not None:
                            driver.execute_script("arguments[0].click();", buttonMore)
                            time.sleep(2.5)

                flightsPage = Soup(driver.page_source, 'lxml').find_all("div", class_="charters-offers-list__item")
                flights = []

                for f in range(len(flightsPage)):
                    flight, polishAirports = self.getFlightPair(flightsPage[f], polishAirports)
                    flights += flight

                for flight in flights:
                    flight.save()
                sum += len(flights)

        except TimeoutException:
            print("Loading took too much time!")

        print("NUMBER OF FLIGHTS: " + str(sum))
        with open('data/airports.json', 'a') as outfile:
            outfile.write(json.dumps(airports, default=str))
        with open('data/polishAirports.json', 'a') as outfile:
            for j in range(len(polishAirports)):
                line = polishAirports[j]
                if j < len(polishAirports) - 1:
                    line += "|"
                outfile.write(line)
        driver.close()

    def getFlightPair(self, flightPairPage, polishAirports):
        first = flightPairPage.find("div", class_="charters-offer-item__wrapper--departure")
        second = flightPairPage.find("div", class_="charters-offer-item__wrapper--return")
        day1 = first.find("span", class_="charters-offer-flight-data__heading-date").text.strip()
        day2 = second.find("span", class_="charters-offer-flight-data__heading-date").text.strip()
        # details[0] - departure, details[1] - arrival
        details1 = first.find_all("div", class_="charters-offer-flight-data__details")
        details2 = second.find_all("div", class_="charters-offer-flight-data__details")
        # 0 - time, 1 - airportName
        dep1 = details1[0].find_all("span")
        arr1 = details1[1].find_all("span")
        dep2 = details2[0].find_all("span")
        arr2 = details2[1].find_all("span")
        travelTime1 = first.find_all("span", class_="charters-offer-flight-data-description__value")[0].text.strip()
        travelTime2 = first.find_all("span", class_="charters-offer-flight-data-description__value")[0].text.strip()

        if dep1[1].text.strip() not in polishAirports:
            polishAirports += [dep1[1].text.strip()]
        if arr1[1].text.strip() not in polishAirports:
            polishAirports += [arr1[1].text.strip()]
        if dep2[1].text.strip() not in polishAirports:
            polishAirports += [dep2[1].text.strip()]
        if arr2[1].text.strip() not in polishAirports:
            polishAirports += [arr2[1].text.strip()]

        flightPair = [FlightTemplate(departure_airport=dep1[1].text.strip(), arrival_airport=arr1[1].text.strip(),
                                     date=day1, travel_time=travelTime1,
                                     departure_time=dep1[0].text.strip(), arrival_time=arr1[0].text.strip()),
                      FlightTemplate(departure_airport=dep2[1].text.strip(), arrival_airport=arr2[1].text.strip(),
                                     date=day2, travel_time=travelTime2,
                                     departure_time=dep2[0].text.strip(), arrival_time=arr2[0].text.strip())]

        return flightPair, polishAirports
