## web test for viewing the time/deadline of the project
click("1413915597017.png")
find("1413834050138.png")
click("1413834060620.png")
type("henry-test.firebaseapp.com/projects" + Key.ENTER);
find("1415127384336.png")
click("1415127384336.png")
if exists("1415127413712.png"):
    find("1415127413712.png")
    print("Due date found for henry project")
else:
    print("Henry project due date not found")

click("1415127584618.png")
find("1415127597657.png")
click("1415127597657.png")
if exists("1415127628828.png"):
    find("1415127664601.png")
    print("Due date found for Mahout project")
    print("Due dates found, test complete.")
else:
    print("Mahout project due date not found")