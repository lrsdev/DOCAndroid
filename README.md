# Dogs on Beaches Android Application

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

Please see the wiki for further information.
