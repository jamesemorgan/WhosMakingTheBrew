Main goals
==========

Dead Features - Possibly to Return
==================================
* [ ] Tea Timer?
* [ ] Widget?
* [ ] Adverts?
* [ ] Sharing Results?

Tech Debt
=========

* [ ] Consider moving to GreenDao from ORMLite - https://github.com/greenrobot/greenDAO
* [ ] EventBus for Communication - https://github.com/greenrobot/EventBus
* [ ] Move remaining libs/jars to gradle
* [ ] Google Analytics
* [ ] Update to latest ACRA - consider options for crash reporting
* [ ] Store LogBack logs & send on crash
* [ ] Update package to allow for new version of application
* [ ] Fix all lint errors

Feature Rework
==============
* [ ] All graphics / styling
* [ ] Spinning tea pot
* [ ] Internationalise
* [ ] Animation between activities
* [ ] Look up contacts from phone
* [ ] Log in with social accounts?
* [ ] Add picture from contact not emojis
* [ ] General list of players
* [ ] Results
* [ ] Groups
* [ ] Latest Android Version Support
* [ ] Option to remove scores from players with option to view via preference
* [ ] Backup data on Google Backup?

Old Rework
==========

Remove dead features:
- [x] Tea timer (maybe replaced one day!)
- [x] Pointless widget etc
- [x] Remove any advertisements
- [x] Remove commons-lang - replaced with copy of source class from commons-lang
- [x] Remove email results feature

Keep main features including:
- [x] Running a game of selected players
- [x] Ability to group players into brew groups
- [x] Ability to record and view stats on players and brew groups

Code changes

- [x] Rework groups view
- [x] Rework stats view
- [x] Rework feedback form
- [x] Rework Settings
- [x] Rework Google Analytics integration and what is recorded
- [x] Remove abstract activities  
- [x] Rework Credits
- [x] Replace deprecated dialogs with DialogFragments
- [x] Add prompt to rate application on google play
- [x] Set target version to latest android version (19)
- [x] Set minimum version to android version (11)
- [x] Alter domain logic and clean up Brew*.java 
- [x] Update to latest Ormlite - 4.48
- [x] Update to latest ACRA, is it the best option - 4.5.0
- [x] Delete group when no players left
- [x] Replace customer logger with logback-android
- [x] Change build system to use Gradle

-- Groups
- [x] Long click groups, Open Group / Delete Group