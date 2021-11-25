# My Personal Project: Data Manipulator

Sometimes it is difficult and annoying to manipulate a bunch of data, especially for data in different files. 
This application helps users who want to manipulate data to do so.

## User Stories
### Some *functions* that user could do:
- *import* csv files and save as Data class \**
- *specify* data types of each column in the csv files
- *specify* index column of Data
- do *arithmetics (+,-,\*,/)* on columns
- *concatenate* csv files
- *add/remove* Column(s) to Data
- *add/remove* new entry (row) to Data
- *view* Data
- *save* Data as .json **
- *load* Data by .json **

**: users can import/load/save files under *./data/*

---
###Phase 4: Task 2
A representative sample of the events that occur when the program runs:
```
Wed Nov 24 13:59:08 PST 2021
Column "Name" is added.

Wed Nov 24 13:59:08 PST 2021
Column "CSID" is added.

Wed Nov 24 13:59:08 PST 2021
Column "Age" is added.

Wed Nov 24 13:59:08 PST 2021
Column "Height" is added.
```
---
###Phase 4: Task 3
If you had more time to work on the project, is there any refactoring that you would do to improve your design? 
- If I had more time, I would have applied composite pattern on "Data" and "Column", 
so that "Data" can contain "Data" and "Column", 
and thus easier for concatenating "Data".