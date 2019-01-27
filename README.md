# aws-smf
(basic) implementation of securities master file service using aws services
This project is just an example developed for training purposes, so not intended to go to production.
Some implementation may not comply with business requirements and data may be 'simple'

## Structure
- aws-smf
  - build: resources to setup the environment and platform (development and execution)
  - core : shared source code, utility classes
  - functions: root module for functions (lambda)
      - search: source code for query functions
      - load: source code for data loading functions
