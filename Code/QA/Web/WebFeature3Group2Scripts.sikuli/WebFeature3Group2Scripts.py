## Testing the status of milestone(s) in the project
click("1413915597017.png")
find("1413834050138.png")
click("1413834060620.png")
type("henry-test.firebaseapp.com/projects" + Key.ENTER);

find("1413915942887.png")
click("1413915949955.png")
exists("1413915964434.png")
if exists("1413915701068.png"):
    click("1413915708480.png")
    if exists("1413915727080.png"):
        print("It worked")
    else:
        print("It didn't work")
else:
    print("It didn't work")

 
## Testing the status of the project as a whole
click("1413836181760.png")
find("1413834175850.png")
click("1413834184376.png")
find("1413836030795.png")
click("1413836047701.png")
if exists("1413836085141.png"):
    print("It worked")
else:
    print("It didn't work");

## Testing the ability to update a task's status
click("1413836181760.png");
find("1413834175850.png")
click("1413834184376.png")
find("1413916721723.png")
click("1413916727908.png")
find("1415040680478.png")
click("1415040690013.png")
find("1413927687300.png")
click("1415040760485.png")
find("1413927542743.png")
click("1413927569377.png")
if exists("1415040403501.png"):
        print("It worked")
else:
        print("It didn't work")
print("Finished")


