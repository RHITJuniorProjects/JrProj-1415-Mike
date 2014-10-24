## iOS Sikuli script
## Testing ability to see the status of milestones in a project
click("1414079655410.png")
find("1414079698083.png")
click("1414079706441.png")
if exists("1414079730628.png"):
    print("It worked")
else:
    print("It didn't work")
    
## Reset to starting position
while exists("1414079783514.png"):
    click("1414079783514.png")

## Test ability to see the progress of the project

find("1414079655410.png")
click("1414080125567.png")
find("1414080151835.png")

## Reset to starting position
while exists("1414079783514.png"):
    click("1414079783514.png")

## No current way to test changing status
    

