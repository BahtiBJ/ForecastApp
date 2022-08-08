# ForecastApp
Android application for displaying weather forecasts for the cities of Kazakhstan.
# Capabilities

At startup, a window appears that requests an api key.

The main screen consists of 2 pages, switching between with the help  of animated tabs (Lottie animations).

![](https://github.com/BahtiBJ/ForecastApp/blob/master/illustration/show_tabs.gif)

The first page contains the selected city, temperature, short description, pressure, density and wind speed.
When displaying data from the server in the lower right corner, depending on the temperature and precipitation, a certain animation is played.

![](https://github.com/BahtiBJ/ForecastApp/blob/master/illustration/show_forecast.gif)

On the second page there is a button to select a city. Clicking on it opens a page with a list of available cities.

![](https://github.com/BahtiBJ/ForecastApp/blob/master/illustration/show_list.gif)

# Structure

Data is received through the Retrofit library via the site API https://openweathermap.org/. Working with the database is carried out through the Room library.
The database stores forecasts for cities viewed during the day. Dependency injection implemented by Dagger 2.
