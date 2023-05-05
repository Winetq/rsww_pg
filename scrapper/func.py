import json
import random
import templates
import re
import func


def getLocalization(localization):
    country = localization[0].find("span").text
    if len(localization) == 2:
        city = localization[1].find("span").text
    elif len(localization) == 3:
        city = localization[2].find("span").text
    else:
        city = ""
    return country, city


def getRoomNumberInfo(offerPage):
    hotelInformations = offerPage.find("div", {"id": "accordion__body-HOTEL"}).find_all("div", "odd:rounded-md")
    roomNumberInfo = [""]
    for i in range(len(hotelInformations)):
        if "Liczba pokojów" in hotelInformations[i].find("span").text.strip():
            roomNumberInfo = hotelInformations[i].find_all("li")
            for j in range(len(roomNumberInfo)):
                roomNumberInfo[j] = roomNumberInfo[j].text.strip()
    return roomNumberInfo


# "none|breakfast|two-dishes|three-dishes|all-inclusive"
def getFoodInfo(offerPage):
    foodInformations = offerPage.find("div", {"id": "accordion__body-FOOD"}) \
        .find_all("article", class_="OfferDetailsBlock_offerDetailsBlock__EX2Zv")
    foodTitle = [0] * len(foodInformations)
    for i in range(len(foodInformations)):
        foodTitle[i] = foodInformations[i].find("h3", class_="heading").text.strip()
    food = "none"
    for i in range(len(foodTitle)):
        if "all inclusive" in foodTitle[i].lower():
            food += "|all-inclusive"
        elif "trzy posiłki" in foodTitle[i].lower():
            food += "|three-dishes"
        elif "dwa posiłki" in foodTitle[i].lower():
            food += "|two-dishes"
        elif "śniadanie" in foodTitle[i].lower():
            food += "|breakfast"
    return food


def getRoomNumber(offerPage):
    roomNumberStr = func.getRoomNumberInfo(offerPage)[0]
    if roomNumberStr.isnumeric():
        return int(roomNumberStr)
    roomsInfo = re.split(':|,| ', roomNumberStr)
    roomNumber = 0
    for i in range(len(roomsInfo)):
        if roomsInfo[i].isnumeric():
            roomNumber += int(roomsInfo[i].strip())
    if roomNumber == 0:
        roomNumber = random.randrange(start=20, stop=100)
    return roomNumber


def getRoomInfo(offerPage, stars):
    roomNumber = getRoomNumber(offerPage)
    roomInformations = offerPage.find("div", {"id": "accordion__body-ROOMS"}) \
        .find_all("article", class_="OfferDetailsBlock_offerDetailsBlock__EX2Zv")
    roomTitle = [0] * len(roomInformations)
    features = [""] * len(roomInformations)
    capacity = [0] * len(roomInformations)
    for i in range(len(roomInformations)):
        roomTitle[i] = roomInformations[i].find("h3", class_="heading").text.strip()

        roomFeatures = \
            roomInformations[i].find_all("li", class_="OfferDetailsBlock_offerDetailsBlockFeature__Xztov")
        for j in range(len(roomFeatures)):
            if "liczba osób" in \
                    roomFeatures[j].find("span", class_="OfferDetailsBlock_offerDetailsBlockFeatureName__cviXr").text:
                capacity[i] = len(roomFeatures[j].find_all("svg"))
                break
        if capacity[i] == 0:
            capacity[i] = random.randrange(2, 4)

        roomContent = \
            roomInformations[i].find_all("li", class_="OfferDetailsBlock_offerDetailsBlockSpecsListItem--room__Pg3R4")
        for j in range(len(roomContent)):
            features[i] += roomContent[j].find("span").text.strip()
            if j < len(roomContent) - 1:
                features[i] += "|"

    rooms = [0] * len(roomTitle)
    numberOfRooms = roomNumber // len(rooms)
    for i in range(len(rooms)):
        if i == len(rooms) - 1:
            numberOfRooms = roomNumber - numberOfRooms * (len(rooms) - 1)
        basePrice = 40 * stars + 10 * capacity[i]
        if "apartament" in roomTitle[i].lower():
            basePrice *= 1.2
        rooms[i] = templates.RoomTemplate(name=roomTitle[i], capacity=capacity[i], features=features[i],
                                          numberOfRooms=numberOfRooms, basePrice=float(int(basePrice)))
    return rooms


def getHotelImages(offerPage, imageNumber):
    images = offerPage.find_all("div", class_="swiper-wrapper")[0].find_all("img")
    images_urls = [0] * imageNumber
    k = 0
    for i in range(len(images)):
        if k < imageNumber:
            if "Hotel" in images[i]['alt'] and "https" in images[i]['src']:
                images_urls[k] = images[i]['src']
                k += 1

    # save images on disk
    # for i in range(len(images_urls)):
    #     response = requests.get(images_urls[i])
    #     with open("images/image" + str(i) + ".jpg", "wb") as f:
    #         f.write(response.content)

    return images_urls


def getHotelAirport(offerPage):
    airport = offerPage.find("div", {"id": "accordion__body-FLIGHT_INFO"})\
        .find_all("li", class_="flight-info-wrapper__item")
    airport = airport[0].find("div", class_="flight-info__part").find_all("span").pop().text.strip()
    return airport


def getHotelAirportBasedOnCountry(country):
    with open('data/airports.json', 'r') as outfile:
        airports = json.load(outfile)
        hotelAirports = []
        for airportName, airportCountry in airports.items():
            if airportCountry == country:
                hotelAirports += [airportName]
        hotelAirport = random.choice(hotelAirports)

    return hotelAirport
