Table of Contents
=================
* [Introduction](#introduction)
* [Features](#features)

## Introduction
Dogs on Beaches is a platform which provides near real-time information regarding dog regulations pertaining to our local beaches. Marine wildlife has long suffered at the jaws of roaming dogs, often resulting in serious injury or death. This application is intended to supplement traditional information methods such as signage with a dynamically updatable real time platform intended to encourage public participation, increase awareness of the dangers dogs pose to our marine widlife, and ultimately drive down these attacks.

This application was developed in conjunction with the New Zealand [Department of Conservation](http://www.doc.govt.nz) as part of a third year student development project at [Otago Polytechnic](http://www.op.ac.nz) in Dunedin, New Zealand.

## Features
* A map indicating beaches, marker icon represents the beaches 'dog' status (on lead, off lead, no dogs)
* Location detail screen. States supplmentary dog guidelines, wildlife information specific to that location.
* Wildlife reporting. Users can photograph and lodge wildlife reports.
* Wildlife identification information.
* Generic dog guidelines
* Local wildlife group donations
* Functions without network connectivity, synchronises periodically when the network is available.

## Offline Availability
Dogs on Beaches functions offline by periodically synchronising new information from a [web service.](http://github.com/lrsdev/dog-rails) This functionality is implemented with a local SQLite database, a content provider, stub authenticator and a synchronisation adapter.

## Tech Overview

### First Run
The application checks for the presence of a key in shared preferences, if it isn't present a first run setup is executed. The initial setup operation includes:
* Unzipping a packaged zip file containing application assets. (see assets)
* Setting up automatic synchronisation with the android framework. (see synchronisation)
* Quietly initiates an initial remote synchronisation.
* Adds a first_run key to shared preferences with false value.

### Map
The MapFragment checks if the user is connected to a network. If not, a map is initialised using packaged map tiles. If the application can't ascertain the users location, or it is outside of the offline map's bounding box, the app will show an alert and the user will be centred in Dunedin, New Zealand. If the user is online, an online map will be initialised using MapBox's tile server. If user location is unobtainable, they will be automatically centred in Dunedin. Map Markers are populated from location information stored in the applications database, through a content provider. Clicking on a marker will open an activity detailing extra information for that location.

### Locations 
This is a RecyclerView using cards to display location information. Locations are ordered in ascending order by distance from user. SQLite lacks trigonometric functions, so locations are ordered by the basic manhatten formula which favours locations near the users axis, so this ordering is currently not perfect.

### Reporting
The report screen presents two spinners, one for location, one for animal type. The Location spinner will be populated from the database with the five locations closest to the user. Additionally, an Other option is provided if their location isn't present in our database. The Animal spinner is populated with all the animals in the local database. The only animals present in the database are of specific interest to our client, so are limited in number. Again, an other option is provided if the user would like to report some other species. A text field is provided for the user to enter any further information.

The take picture button starts the internal camera application for image capture. Images are stored in the public user directory incase they wish to view them later. When submitting the report, the application will use the last taken image. 

On submit, the application will check it's geolocation, if it cannot obtain this it will not allow the user to submit the report. The report image will be copied from the public directory into the applications internal directory so it can't be removed by the user before the application synchronises the report to the server. Once the report is synchronised the user will receive a simple notification.

### Synchronisation
The application provides a synchronisation adapter with a stub authenticator so we can make use of Android's synchronisation framework. The sync adapter accesses the applications database via a content provider, and communicates with the remote RESTful API using the retrofit api client. On the applications first run, it will setup automatic synchronisation which can be turned on and off in androids accounts section under settings. On each sync, reports are synchronised to server first then a sync request is sent to the remote API with the timestamp of the last synchronisation, the remote server returns a json object with all changes since the supplied timestamp.

Report Sync Logic:
* Get cursor from database, if reports present, continue.
* Iterate cursor
* Create an api request with report details, send
* If request object returns code 201, delete report from local database and remove image.

Location/Animal Sync Logic:
* Get ArrayLists of updated records and deleted records from sync object.
* Build a hashmap of updated animals with their id as the key, the object as the value.
* Iterate cursor containing all local records
  * Check if the current record id is in the sync objects arraylist representing deleted records, if it is, add the delete operation to a batch update operation. Add the record's local image to a filesToDelete array.
  * Check if current record id is in the hashmap, if it is, create a batch update operation with the updated details. If the updated record contains a new image, download the new image and add the file to a filesCreated array, add the current image to a filesToDelete array.
  * Remove the record from the hashmap.
* All records left in hashmap should be new records, iterate the hashmap creating a batch operation for each new record and download the image. New record images are also added to the filesCreated array.
* Apply batch operation to commit all changes with a single access operation. If it fails to execute, deleted all downloaded images associated with the new records. If it succeeds, delete the files attached to the old records.
* Close all cursors, update the last synced timestamp in the database with the timestamp provided by the remote sync object.

### Building 
#### Assets
Because our application's use cases requires data to be available offline, and for optimal user experience we package a snapshot of the remote data and the map tiles database with each release. Without packaging this data with the apk, the user would need to synchronise all required remote data to the device on first run. If the user were to initially run the application in a location without network connectivity, the application would contain none of the information they seek. Furthermore, we don't envisage the location or animal images will change often, these would make up a large part of the initial synchronisation so it makes sense to package this data in the initial download.

All the applications assets are packaged in an assets.zip, these zip files is extracted on the application's first run to the app's root directory.

#### Build Variants
The application has two gradle flavours, dev and prod. Which gives the application four build variants. devRelease, devDebug, prodRelease, prodDebug. Variants can be installed side by side. The prod variants differ from the dev variants only by which API endpoint they communicate with and the prepackaged assets. This allows us to easily test new features and change data around on the development api without affecting the production versions.

Please see the wiki for further information.
