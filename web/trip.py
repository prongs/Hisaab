import yaml
import solver


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
            self.people = list(Person(key, *map(int, self.people[key].split())) for key in self.people)

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
