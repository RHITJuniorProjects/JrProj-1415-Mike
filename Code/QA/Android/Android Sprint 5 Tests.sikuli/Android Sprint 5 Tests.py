##Tests ran from user already signed in
##Test view all members of a project
click("1418505364112.png")
wait(5)
click("1418505383333.png")
wait(5)
if exists("1418505404192.png"):
    print("It worked")
else:
    print("It failed")


