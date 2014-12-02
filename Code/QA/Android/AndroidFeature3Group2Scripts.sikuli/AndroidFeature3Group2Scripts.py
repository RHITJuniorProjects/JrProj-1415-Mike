## Android test scripts
## Test ability to see status of milestones of the project
## Only works by manually changing isPasswordValid function in LoginActivity.java to >1

find("1413993915191.png")
click("1413993915191.png")
wait(5)
type("test2@test.com" + Key.TAB + "test")
click("1415309656716.png")
sleep(10)
click("1415309819206.png")
wait(5)
find("1413937690035.png")
click("1413937698349.png")
wait(5)
find("1415808528637.png")
click("1415808534069.png")
wait(5)
if exists("1415808554919.png"):
    find("1415808572579.png") 
    print("It worked")
else:
    print("It didn't work")
for x in range(0,3):
    click("1415310085914.png") 


wait(5)


## Test ability to see status of the project

find("1415309819206.png")
click("1415309819206.png")
if exists("1415808600160.png"):
    find("1415808616979.png")
    print("It worked")
else:
    print("It didn't work")
wait(3)

click("1415310142817.png")

wait(5)

## Test the ability to update the status of a feature

find("1415309819206.png")
click("1415309819206.png")
wait(5)
find("1413937690035.png")
click("1413937698349.png")
wait(7)
find("1415808641967.png")
click("1415809089092.png")
wait(5)
find("1415310212774.png")
click("1415310212774.png")
wait(5)
click("1415310252326.png")
wait(5)
find("1413938557144.png")
click("1413938557144.png")
wait(2)
find("1415310282241.png")
click("1415310288811.png")
if exists("1415310319977.png"):
    print("It worked")
else:
    print("It didn't work")

print("Finished")