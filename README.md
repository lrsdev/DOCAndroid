Table of Contents
=================
* [Introduction](#introduction)
* [Features](#features)
* [Synchronisation](#synchronisation)
* [Building](#building)

## Introduction
Dogs on Beaches is a platform which provides near real-time information regarding dog regulations pertaining to Dunedin beaches. Marine wildlife has long suffered at the jaws of roaming dogs, often resulting in serious injury or death. This application is intended to supplement traditional information methods such as signage with a dynamically updatable real time platform intended to encourage public participation, increase awareness of the dangers dogs pose to our marine widlife, and ultimately drive down these attacks.

This application was developed in conjunction with the New Zealand [Department of Conservation](http://www.doc.govt.nz) as part of a third year student development project at [Otago Polytechnic](http://www.op.ac.nz) in Dunedin, New Zealand.

## Features
* A map indicating beaches, marker icon represents the beaches 'dog' status (on lead, off lead, no dogs)
* Location detail screen. States supplmentary dog guidelines, wildlife information specific to that location.
* Wildlife reporting. Users can photograph and lodge wildlife reports.
* Wildlife identification information.
* Generic dog guidelines
* Local wildlife group donations
* Functions without network connectivity, synchronises periodically when the network is available.

## Synchronisation
The application provides a synchronisation adapter with a stub authenticator so we can make use of Android's synchronisation framework. The sync adapter accesses the applications database via a content provider, and communicates with the [remote API](https://github.com/lrsdev/dog-rails) using the RetroFit API client. On first run, it will setup automatic synchronisation which can be turned on and off in Android'ss accounts section under settings. On each sync, reports are synchronised to server first then a sync request is sent to the remote API with the timestamp of the last synchronisation, the remote server returns a json object with all changes since the supplied timestamp.

## Building 
### Assets
Because our application's use cases requires data to be available offline, and for optimal user experience we package a snapshot of the remote data and the map tiles database with each release. Without packaging this data with the apk, the user would need to synchronise all required remote data to the device on first run. If the user were to initially run the application without network connectivity, the application would contain none of the information they seek.

Applications assets are packaged in an assets.zip, these zip files is extracted on the application's first run to the app's root directory. A [repository](https://github.com/lrsdev/android-data/) is maintained containing this data.

#### Obtaining
##### Remote data
The easiest way to obtain assets for packaging is to run the application on an emulator (fresh installation), force a synchronisation with the server and pull the assets from the emulator using the adb command line tool. This process should be performed twice, once for the prod build flavor, the other for the dev build flavor, if the remote API endpoints are different.

For example, to get production data:
`adb pull /data/data/io.github.lrsdev.dogbeaches.prod/databases databases`
`adb pull /data/data/io.github.lrsdev.dogbeaches.prod/files files`

##### Map Tiles
The application expects a 'otago.mbtiles' file to be present in the databases directory. Mbtiles map files can be created with TileMile or MOBAC. Once the mbtiles is obtained, it should be added to the databases directory pulled from the emulator.

#### Packaging
Zip the databases (including the mbtiles map file) into assets.zip. Add this file to the relevant build config's assets directory. The zip file structure should look like:
```
|databases
|  docdog.db
|  docdog.db-journal
|  otago.mbtiles
|files
|  images
|     animals
|        1.jpg
|        2.jpg
|     locations
|        1.jpg
|        2.jpg
```
The .jpg's filename is the location's id in the remote database.

### Build Variants
The application has two gradle flavours, dev and prod. Which gives the application four build variants. devRelease, devDebug, prodRelease, prodDebug. Variants can be installed side by side. The prod variants differ from the dev variants only by which API endpoint they communicate with and the prepackaged assets.
