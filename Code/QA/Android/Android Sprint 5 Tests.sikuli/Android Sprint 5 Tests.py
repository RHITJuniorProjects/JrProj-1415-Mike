##Tests ran from user already signed in
##Test view all members of a project
click("1418505364112.png")
wait(5)
click("1418578049923.png")
wait(5)
if exists("1418578208308.png"):
    print("It worked")
else:
    print("It failed")
##End test

click("1418578072878.png")

##Begin for view all tasks assigned for me
find("1418577707393.png")
click("1418577736612.png")
if exists("1418577811956.png"):
    print("It worked")
else:
    print("It failed")
##End test
click("1418578181442.png")
wait(3)

##Begin test for making sure my tasks are flagged
click("1418578937320.png")
dragDrop("1418579318458.png","1418579324512.png")
wait(3)
click("1418579373502.png")
click("1418579412064.png")
click("1418579428225.png")
if exists("1418579446737.png"):
    print("It worked")
else:
    print("It failed")
##End Test
for n in range(4):
    click("1418579641790.png")



