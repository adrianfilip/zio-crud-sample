1. You can start the app with:
sbt "run local"

3. You will be prompted to select an operartion to perform:

Please select next operation to perform:
1 for Create
2 for Read
3 for Update
4 for Delete
5 for GetAll
6 for ExitApp

3. You can pick the operation by typing the number specified in the prompt. You may be prompted to insert data. 
It will always provide feedback to your operation regardless if it was a success or failure.

Example of some usage:


Please select next operation to perform:
1 for Create
2 for Read
3 for Update
4 for Delete
5 for GetAll
6 for ExitApp
5
Succeeded with List()
Please select next operation to perform:
1 for Create
2 for Read
3 for Update
4 for Delete
5 for GetAll
6 for ExitApp
1
Please insert Employee first name:
Adrian
Please insert Employee last name:
Filip
Succeeded with Employee(afilip,Adrian,Filip)
Please select next operation to perform:
1 for Create
2 for Read
3 for Update
4 for Delete
5 for GetAll
6 for ExitApp
1
Please insert Employee first name:

Please insert Employee last name:

Failed with: InvalidInput(,)