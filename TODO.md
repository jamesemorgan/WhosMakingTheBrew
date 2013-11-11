Main goals
==========

Code changes
- [ ] Modernise and clean code
- [x] Set target version to latest android version (19)
- [x] Set minimum version to android version (11)
- [ ] Alter domain logic and clean up Brew*.java 
- [ ] Make project work with latest version of Android
- [ ] Update all libraries it uses, including assessing alternatives
- [ ] Update to latest Ormlite
- [ ] Update to latest ACRA, is it the best option
- [ ] Rework what is reported by ACRA
- [ ] Change build system to use Gradle
- [x] Replace customer logger with logback-android
- [ ] Rework add players view
- [ ] Rework animation view
- [ ] Rework results view
- [ ] Rework groups view
- [ ] Rework stats view
- [ ] Rework feedback form
- [x] Rework Settings
- [x] Rework Google Analytics integration and what is recorded
- [x] Remove abstract activities  
- [ ] Replace alerts and dialogs with DialogFragments, remove deprecation
- [ ] Add details about this to verison and logs http://source.android.com/devices/tech/dalvik/art.html

Remove dead features:
- [x] Tea timer (maybe replaced one day!)
- [x] Pointless widget etc
- [x] Remove any advertisements
- [x] Remove commons-lang - replaced with copy of source class from commons-lang
- [x] Remove email results feature

Keep main features including:
- [ ] Running a game of selected players
- [ ] Ability to group players into brew groups
- [ ] Ability to record and view stats on players and brew groups

Gui changes
- [ ] Improve animation during game runner
- [ ] Remove scores from players with option to view via preference
- [ ] Remove unused images and assets
- [ ] Internationalise and strings
- [ ] Fix all lint errors, within reason
- [ ] Animate between views - http://developer.android.com/training/animation/index.html


Questions
- [ ] What does allowBackUp mean?

