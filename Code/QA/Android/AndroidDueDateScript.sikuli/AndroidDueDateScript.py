#checking deadlines
#using user "test2@test.com" "test"
#using project "Henry - Platform"
click("1415307619657.png")
if exists("1415307653784.png"):
    print("Showing due date successfully")
else:
    print("ERROR: Due date not showing successfully")
click("1415307796627.png")

#using project "iOS 7"
click("1415307823458.png")
if exists("1415307847224.png"):
    print("Showing due date successfully")
else:
    print("ERROR: Due date not showing successfully")