# Android-Restaurant-Finder
Description of Restaurant App.

In this app we have developed a restaurant finding application. This application gives two options. One is finding restaurants nearby to current location. Another feature is finding restaurants nearby to custom search location.
The restaurants are listed in a list. The restaurant with highest rating comes on top of the list. A user can view the locations of these restaurants on a map which has zoom and current location features. 

Description of features included.

Show MapView 
Map should focus on the current location
Use TabLayout/Navigation Drawer
Proper markers for showing current and nearby cafe
SearchBar with search functionality
Use RecyclerView to list nearby cafe for the second activity
Nearby cafe listings should be ordered based on their reviews
Use SnackBar to show any message/error
Proper error handling, should be able to handle network connectivity errors, absent Google Play Services Framework error, an absent GPS sensor, and missing permissions
Use of Material theme(Material Design Theme)

Application Home Screen

The application shows nearby restaurants to your current location or the location you have searched. There are two options on home screen. By Current Location button gives you the option of searching restaurants near your current location. By Search button gives you the option to search any location and then lists the restaurants near that location. Homescreen also gives you the ability to focus on current location.

Current Location

User can access his current location from homescreen as well as from the By Current location screen. It displays the marker at the current location of the user. 

Snake Bar

On home screen when a user selects one of the options to search restauransts, Snakebar pops up to confirm the choice.

Custom Search

Custom search activity gives the user the option to search any place and then get the list of restaurants nearby. Enter a location in text field and then press Search button to get to that location on map.

Restaurants Nearby Custom Search Location

User can use the SHOW ON MAP button to see the locations of the restaurants on a map. Searched location is shown by Red marker and restaurants are shown by blue markers. On clicking any marker the name of the restaurant is displayed.

Restaurants Nearby Current Location

Current location search screen gives user two buttons. User can use the Get Address button to see the current locations and get its address. Get Restaurants button displayes the list of restaurants ordered by ratings. Location of the restaurants nearby to current location displayed on a map. Current location is shown by Red marker and restaurants are shown by blue markers. On clicking any marker the name of the restaurant is displayed

