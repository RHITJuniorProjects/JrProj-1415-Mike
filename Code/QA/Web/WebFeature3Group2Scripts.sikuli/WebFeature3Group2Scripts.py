## Testing the status of milestone(s) in the project
click("1413915597017.png")
find("1413834050138.png")
click("1413834060620.png")
type("henry-test.firebaseapp.com/projects" + Key.ENTER);

find("1415380272421.png")
click("1415380407005.png")
exists("1415380185063.png")
if exists("1413915701068.png"):
    click("1415380200767.png")
    if exists("1415380232210.png"):
        print("It worked")
    else:
        print("It didn't work")
else:
    print("It didn't work")

 
## Testing the status of the project as a whole
click("1413836181760.png")
find("1415380455013.png")
click("1415380461436.png")
find("1415380431586.png")
click("1415380436857.png")
if exists("1415380444672.png"):
    print("It worked")
else:
    print("It didn't work");

## Testing the ability to update a task's status
click("1413836181760.png");
find("1415380481779.png")
click("1415380487737.png")
find("1415380496285.png")
click("1415380502691.png")
find("1415380557013.png")
click("1415381107608.png")
find("1415381121714.png")
click("1415381130237.png")
find("1413927542743.png")
click("1413927569377.png")
if exists("1415380547528.png"):
        print("It worked")
else:
        print("It didn't work")
print("Finished")


