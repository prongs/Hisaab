import yaml
import solver
import re

inteval = lambda x: x if type(x) == int else int(eval(x))


class Person(object):
    """docstring for Person"""
    def __init__(self, name, share=0, spent=0):
        super(Person, self).__init__()
        self.name = name
        self.share = share
        self.spent = spent
        self.net = self.share - self.spent

    def __repr__(self):
        return "%s: %d %d" % (self.name, self.share, self.spent)

    def repr_net(self):
        suffix = " is balanced!"
        self.net = self.share - self.spent
        if self.net > 0:
            suffix = " will pay %d" % self.net
        elif self.net < 0:
            suffix = " will receive %d" % (-self.net)
        return self.name + suffix


class Event(object):
    """docstring for Event"""
    def __init__(self, name, people):
        super(Event, self).__init__()
        self.name = name
        self.people = people
        if type(self.people) == dict:
            ppl = {}
            for key in people:
                m = re.search(r'-+>+', key)
                if m:
                    p1 = key[:m.start()].strip()
                    p2 = key[m.end():].strip()
                    ppl.setdefault(p1, Person(p1, 0, 0))
                    ppl.setdefault(p2, Person(p2, 0, 0))
                    amt = inteval(people[key])
                    ppl[p1].spent += amt
                    ppl[p2].share += amt
                else:
                    p = key.strip()
                    ppl.setdefault(p, Person(p, 0, 0))
                    share, spent = map(inteval, people[p].split())
                    ppl[p].share += share
                    ppl[p].spent += spent

            self.people = ppl.values()

    def __repr__(self):
        return self.name + ": " + "{" + ", ".join(str(p) for p in self.people) + "}"


class Trip(object):
    """docstring for Trip"""
    def __init__(self, name, events):
        super(Trip, self).__init__()
        self.name = name
        self.events = events
        if type(self.events) == dict:
            self.events = list(Event(key, self.events[key]) for key in self.events)

    def __repr__(self):
        return self.name + ": " + "{" + ", ".join(str(e) for e in self.events) + "}"

    @staticmethod
    def from_yaml(content):
        parsed_content = yaml.load(content)
        trips = list(Trip(trip_name, parsed_content[trip_name]) for trip_name in parsed_content)
        return trips[0]

    def solve(self):
        people = {}
        for event in self.events:
            for person in event.people:
                people.setdefault(person.name, Person(person.name, 0, 0))
                people[person.name].share += person.share
                people[person.name].spent += person.spent
        for p in people:
            people[p].net = people[p].share - people[p].spent
        mysolver = solver.MinSimpleSolver(people.values())
        return (mysolver.summary(), repr(mysolver.solve()))
