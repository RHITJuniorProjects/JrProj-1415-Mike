## Test the ability to check the leaderboard from the homepage
## Test assumes user already signed in and assigned to projects
click("1423538524477.png")
wait(7)
if exists("1423538574276.png"):
    print("It worked.")
else:
    print("It failed.")

##Reset
click("1423597955516.png")
wait(5)

## End Test
## Begin testing the ability to create bounties for tasts
## Test assumes user already signed in and assigned to projects
click("1423598104510.png")
wait(5)
click("1423597400426.png")
wait(5)
dragDrop("1423597431039.png","1423597463374.png")
click("1423597474496.png")
click("1423597485273.png")
click("1423597497686.png")
click("1423597515009.png")
click("1423597528189.png")
find("1423597544185.png")
click("1423597552102.png")
click("1423597565889.png")
type("Test create bounty" + Key.TAB + "Testing Create a bounty")
click("1423597642536.png")
click("1423597647343.png")
find("1423597762116.png")
click("1423597769421.png")
wait(5)
click("1423597780182.png")
wait(3)
click("1423597832693.png")
click("1423597843380.png")
wait(4)
click("1423598403287.png")
click("1423598508536.png")
if exists("1423598520178.png"):
   print("It worked.") 
else:
    print("It failed.")
