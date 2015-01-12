#Starting inside user "hullzr@rose-hulman.edu" password "test"
#from homepage
click(Pattern("1418766072345.png").exact())
click("1418711810267.png")
click("1418711822591.png")
click("1418711839382.png")
click("1418711855624.png")
click("1418711874581.png")
wait(5)
if exists("1418711891381.png"):
    print("Category changed correctly")
    click("1418711891381.png")
    click("1418712059839.png")
else:
    print("ERROR: Category not changed correctly")