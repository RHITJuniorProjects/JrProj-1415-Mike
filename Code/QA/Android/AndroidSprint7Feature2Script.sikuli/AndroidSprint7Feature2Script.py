##Test ability to add game points to an already created task and 
## a newly created task.
##Test assumes user already signed in.
click("1422305702257.png")
wait(5)
click("1422305903376.png")
click("1422305918435.png")
click("1422305934000.png")
click("1422305946137.png")
click("1422305967006.png")
click("1422305986133.png")
if exists("1422306006591.png"):
    print("It worked")
else:
    print("It failed")
##Reset for testing purposes

click("1422306076430.png")
##Now test when creating a task

click("1422306149444.png")
click("1422306167383.png")
type("Sprint 7 Feature 2")
click("1422306204872.png")
type("For testing")
click("1422306238234.png")
click("1422306246991.png")
wait(5)
click("1422306740098.png")
click("1422306770043.png")
click("1422306792187.png")
if exists("1422306814261.png"):
    print("It worked")
else:
    print("It failed")

