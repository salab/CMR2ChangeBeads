# CMR2ChangeBeads
A converter from ChangeMacroRecorder's log file to ChangeBeadsThreader's input

## Usage
1. `$ git clone https://github.com/salab/CMR2ChangeBeads.git`
1. `$ ./gradlew shadowJar`
- generate .jar file into `build/libs`

## Description
`CMR2ChangeBeads` is a converter from ChangeMacroRecorder's log file to ChangeBeadsThreader's input.

## Requirement
- Git
  - It need to be able to call it using `git` commands.
- Kotlin 1.3+
- Java 1.8+

## Installation
1. `$ git clone https://github.com/salab/CMR2ChangeBeads.git`
1. `$ cd cmr2changebeads`
1. `$ ./gradlew shadowJar`
  - Output .jar file to `build/libs/`

## Usage
- `java -jar CMR2ChangeBeads-1.0.0-all.jar [path ...] -d <path>`
- CMR2ChangeBeads parses the file specified in the argument and outputs the ChangeBeadsThreader's input repository. 

## Option
- [path ...]
    - The path of ChangeMacroRecorder's log files
    - CMR2ChangeBeads parses the file specified in the argument and outputs the ChangeBeadsThreader's input repository.
    - If you specify a directory path, all .log files in the directory will be taken as input.
- -d, --dest <path>
    - The output path to the ChangeBeadsThreader's input repository.
- -l, --log (default: false)
    - Output log to standard output.
