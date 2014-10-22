import unittest
import imp

# cannot use standard import due to lack of .py extension
hook = imp.load_source('hook','commit-msg')

class ParseTestCase(unittest.TestCase):
    
    def test_all_present(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [task:Fill out GoogleDoc] [status:New]'
        expected = ['2','Milestone 32','Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_hours(self):
        msg = 'abc [milestone:Milestone 32] [task:Fill out GoogleDoc] [status:New]'
        expected = [None,'Milestone 32','Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_milestone(self):
        msg = 'abc [hours:2] [task:Fill out GoogleDoc] [status:New]'
        expected = ['2',None,'Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)
    
    def test_missing_task(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [status:New]'
        expected = ['2','Milestone 32',None,'New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_status(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [task:Fill out GoogleDoc]'
        expected = ['2','Milestone 32','Fill out GoogleDoc',None]
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_all(self):
        msg = 'abc'
        expected = [None,None,None,None]
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

if __name__ == '__main__':
    unittest.main()
