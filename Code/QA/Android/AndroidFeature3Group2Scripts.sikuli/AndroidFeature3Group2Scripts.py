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
find("1415309875565.png")
click("1415309881154.png")
wait(5)
if exists("1415309895947.png"):
    print("It worked")
else:
    print("It didn't work")
for x in range(0,3):
    click("1415310085914.png") 


wait(5)


## Test ability to see status of the project

find("1415309819206.png")
click("1415309819206.png")
if exists("1415310130987.png"):
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
find("1415310194585.png")
click("1415310194585.png")
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