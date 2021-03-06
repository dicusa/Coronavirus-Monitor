# Coronavirus-Monitor-Application
Its an Android application to track cases related Covid-19 cases across the world. It fetch data from Rapid Api(website).

## Application Overview
![Coronavirus Monitor Application](images/ApplicationOverview/20200329_160926.gif)

## Application Description
1. It uses Google Maps to plot the markers on countries which are affected by Covid-19.
2. It fetches data from API : - https://rapidapi.com/astsiatsko/api/coronavirus-monitor. You can visit and read the documentation about the API used.
3. It provides a search bar to directy navigate to the specific country.
4. It provides a detailed information about each affected country such as:
 4.1 Total cases
 4.2 Deaths
 4.3 Total Recovered
 4.4 New Cases
 4.5 New Deaths
 4.6 Serious Critical 
 4.7 Active Cases
 4.8 Total Cases per 1M population

### To use Rapid API
1. Visit https://rapidapi.com/astsiatsko/api/coronavirus-monitor and read the documentation.
2. Create your account and generate your API Key and save it.
3. You can paste your API Key in the specific location: \app\src\main\java\com\example\coronachecker\api

### To use Google Maps feature(In Android Studio)
1. Install the Google Play services SDK from SDK manager.
2. Create a Google Maps Activity inside your project.
3. Get a Google Maps API key. Ther are two ways to get it:
 * The fast, easy way: Use the link provided in the google_maps_api.xml file that Android Studio created for you:
	- Copy the link provided in the google_maps_api.xml file and paste it into your browser. The link takes you to the Google Cloud Platform Console and supplies the required information to the Google Cloud Platform Console via URL parameters, thus reducing the manual input required from you.
	- Follow the instructions to create a new project on the Google Cloud Platform Console or select an existing project.
	- Create an Android-restricted API key for your project.
	- Copy the resulting API key, go back to Android Studio, and paste the API key into the <string> element in the google_maps_api.xml file.
 * A slightly less fast way: Use the credentials provided in the google_maps_api.xml file that Android Studio created for you:
	- Copy the credentials provided in the google_maps_api.xml file.
	- Go to the Google Cloud Platform Console in your browser.
	- Use the copied credentials to add your app to an existing API key or to create a new API key.
