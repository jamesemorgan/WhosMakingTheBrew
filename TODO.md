Main goals
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
- [ ] Style Player list text
- [ ] Style Group list header

-- Home
- [ ] Set smiley image on home screen based on scores

-- RUnning
- [ ] Rework animation view
- [ ] Look at removing this and replacing it with overlay?

-- Results
- [ ] Rework results view
- [ ] Check results scroll

-- Structural / Other
- [ ] Make project work with latest version of Android
- [ ] Update all libraries it uses, including assessing alternatives
- [ ] Rework what is reported by ACRA
- [ ] Store Logback Logs in getCacheDir() -> on Crash send logs as part of ACRA
- [ ] Look at setting up https://github.com/ACRA/acralyzer
- [ ] Add details about this to version and logs http://source.android.com/devices/tech/dalvik/art.html
- [ ] Internationalise and strings
- [ ] Fix all lint errors, within reason
- [ ] Animate between views - http://developer.android.com/training/animation/index.html
- [ ] Improve animation during game runner
- [ ] Remove scores from players with option to view via preference
- [ ] Remove unused images and assets

Questions
- [ ] What does allowBackUp mean?

