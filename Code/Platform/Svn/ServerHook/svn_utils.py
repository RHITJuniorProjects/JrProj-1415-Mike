# returns (added,removed)
def loc(diff):
    diffs = diff.split('@@')
    added = 0 ; removed = 0
    for i in range(2,len(diffs),2):
        added = added + diffs[i].count('\n+') - diffs[i].count('\n+++')
        removed = removed + diffs[i].count('\n--') - diffs[i].count('\n---')
    return added,removed
