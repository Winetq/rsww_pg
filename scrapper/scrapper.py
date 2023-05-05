from flight_scrapper import FlightScrapper
from offer_scraper import OfferScrapper

URL = "https://www.tui.pl/"

FlightScrapper(URL=URL, click_more_times=4)
print("End of scrapping flights")
OfferScrapper(URL=URL, offer_number=300)
print("End of scrapping hotels")
