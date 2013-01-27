from trip import *


class UnbalancedException(Exception):
    pass


class Solution(object):
    """docstring for Solution"""
    def __init__(self):
        super(Solution, self).__init__()
        self.tList = []

    def put(self, fr, to, amount):
        if amount != 0:
            self.tList.append((fr, to, amount))

    def len(self):
        return len(self.tList)

    def __repr__(self):
        if len(self.tList) == 0:
            return "All balanced!"
        return "\n".join("%s pays %s %d" % (t[0], t[1], t[2]) for t in self.tList)


class Solver(object):
    """docstring for Solver"""
    def __init__(self, people):
        super(Solver, self).__init__()
        self.people = people
        self.verifySum()

    def cmpNet(self, p1, p2):
        return p2.net - p1.net

    def verifySum(self):
        totalShare = sum(p.share for p in self.people)
        totalSpent = sum(p.spent for p in self.people)
        if totalSpent != totalShare:
            raise UnbalancedException("totalSpent=%d while totalShare=%d" % (totalSpent, totalShare))
        for p in self.people:
            p.net = p.share - p.spent

    def summary(self):
        return "Total Share = %d\nTotal Spent = %d\n\n" % (sum(p.share for p in self.people), sum(p.spent for p in self.people)) + "\n".join(p.repr_net() for p in self.people)


class SimpleSolver(Solver):
    """docstring for SimpleSolver"""
    def solve(self):
        people = sorted(self.people, cmp=self.cmpNet)
        self.solution = Solution()
        while(len(people) > 0):
            if people[0].net > -people[-1].net:
                people[0].net += people[-1].net
                self.solution.put(people[0].name, people[-1].name, -people[-1].net)
                people = people[:-1]
            elif people[0].net < -people[-1].net:
                people[-1].net += people[0].net
                self.solution.put(people[0].name, people[-1].name, people[0].net)
                people = people[1:]
            else:
                self.solution.put(people[0].name, people[-1].name, people[0].net)
                people = people[1:-1]
        return self.solution


class AnotherSimpleSolver(Solver):
    """docstring for AnotherSimpleSolver"""
    def solve(self):
        people = sorted(self.people, cmp=self.cmpNet)
        self.solution = Solution()
        while(len(people) > 0):
            if people[0].net > -people[-1].net:
                people[0].net += people[-1].net
                self.solution.put(people[0].name, people[-1].name, -people[-1].net)
                people = people[:-1]
            elif people[0].net < -people[-1].net:
                people[-1].net += people[0].net
                self.solution.put(people[0].name, people[-1].name, people[0].net)
                people = people[1:]
            else:
                self.solution.put(people[0].name, people[-1].name, people[0].net)
                people = people[1:-1]
            people.sort(cmp=self.cmpNet)
        return self.solution


class MinSimpleSolver(Solver):
    """docstring for MinSimpleSolver"""
    def solve(self):
        s1 = SimpleSolver(self.people).solve()
        s2 = AnotherSimpleSolver(self.people).solve()
        return s1 if s1.len() < s2.len() else s2


def solve(people):
    return MinSimpleSolver(people).solve()
