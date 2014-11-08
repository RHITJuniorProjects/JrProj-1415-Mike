#checking deadlines
#using user "test2@test.com" "test"
#using project "Henry - Platform"
click("1415322297067.png")
find("1415322334197.png")
click("1415322348452.png")
if exists("1415322403044.png"):
    print("Showing due date successfully")
else:
    print("ERROR: Due date not showing successfully")
click("1415322420345.png")
#using project "iOS 7"
find("1415322458222.png")
click("1415322472368.png")
if exists("1415322517705.png"):
    print("Showing due date successfully")
else:
    print("ERROR: Due date not showing successfully")