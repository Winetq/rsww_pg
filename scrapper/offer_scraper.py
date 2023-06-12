from bs4 import BeautifulSoup as Soup
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
import func
import templates
import time

PAGE = "wypoczynek/wyniki-wyszukiwania-samolot"


class OfferScrapper:
    def __init__(self, URL, image_number=1, offer_number=350, delay=10):
        offersNumber = 0
        op = webdriver.ChromeOptions()
        op.add_argument('headless')
        driver = webdriver.Chrome(options=op)

        url = URL + PAGE
        print(url)
        driver.get(url)
        try:
            offers = []
            # load appropriate number of offers
            while len(offers) < offer_number:
                WebDriverWait(driver, delay).until(EC.presence_of_element_located((By.CLASS_NAME, 'results-container__button')))
                buttonMore = driver.find_element(By.CLASS_NAME, "results-container__button")
                if buttonMore is not None:
                    driver.execute_script("arguments[0].click();", buttonMore)
                    time.sleep(1)
                offers = Soup(driver.page_source, 'lxml').find_all("div", class_="offer-tile-wrapper")

            WebDriverWait(driver, delay).until(EC.presence_of_element_located((By.CLASS_NAME, 'offer-tile-wrapper')))
            for offer in offers:
                # go to offer page
                url = URL + offer.find("div", "offer-tile-header")['data-hotel-url']
                driver.get(url)
                offerPage = Soup(driver.page_source, "lxml")

                # scrapping data
                name = offerPage.find("h1", class_="top-section__hotel-name").text.strip()
                print(str(offersNumber) + "\t" + str(name))
                country, city = func.getLocalization(offer.find_all("li", class_="breadcrumbs__item"))
                stars = len(offer.find_all("li", class_="Rating_item__h0rsh"))
                description_element = offerPage.find("div", "text-block__content").find("strong")
                if description_element:
                    description = description_element.text
                    food = func.getFoodInfo(offerPage)
                    photo = func.getHotelImages(offerPage, image_number)
                    if image_number == 1:
                        photo = photo[0]
                    rooms = func.getRoomInfo(offerPage, stars)

                    isFlight = offerPage.find((By.CLASS_NAME, 'flight-info-wrapper'))
                    if isFlight is not None:
                        airport = func.getHotelAirport(offerPage)
                    else:
                        airport = func.getHotelAirportBasedOnCountry(country)

                    hotelTemplate = \
                        templates.HotelTemplate(name, country, city, stars, description, food, photo, rooms, airport)
                    hotelTemplate.save()
                    offersNumber += 1

        except TimeoutException:
            print("Loading took too much time!")

        print("NUMBER OF HOTELS: " + str(offersNumber))
        driver.close()
