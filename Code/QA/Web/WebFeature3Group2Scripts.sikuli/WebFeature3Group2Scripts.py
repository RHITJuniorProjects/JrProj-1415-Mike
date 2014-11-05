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
find("1415210022969.png")
click("1415210022969.png")
if exists("1415210039242.png"):
    print("It worked")
else:
    print("It didn't work");

## Testing the ability to update a task's status
click("1413836181760.png");
find("1413834175850.png")
click("1413834184376.png")
find("1415210107661.png")
click("1415210107661.png")
find("1415210139519.png")
click("1415210139519.png")
find("1415210158698.png")
click("1415040760485.png")
find("1413927542743.png")
click("1413927569377.png")
if exists("1415210179618.png"):
        print("It worked")
else:
        print("It didn't work")
print("Finished")


