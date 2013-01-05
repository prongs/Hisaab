import sublime
import sublime_plugin
import os
from trip import *
from utils import *


sampleLine = """
################################################
#       Sample:                                #
#       TripName:                              #
#           event1:                            #
#               person1:                       #
#                   share spent                #
#               person2:                       #
#                   share spent                #
#               person1 -> person2:            #
#                   amount_transferred         #
#           event2:                            #
#               ...                            #
################################################
"""


class HisaabCommand(sublime_plugin.ApplicationCommand, sublime_plugin.EventListener):
    def init(self):
        path = os.path.join(sublime.packages_path(), "User", "Hisaab")
        if os.path.exists(path):
            if not os.path.isdir(path):
                raise Exception()
        else:
            mkdir_p(path)
        self.trip_files_location = path

    def run(self, new=False):
        self.init()
        if new:
            self.trip_choice_handler(0)
        else:
            files = map(lambda x: x[:-5], filter(lambda x: x[-4:] == "yaml", os.listdir(self.trip_files_location)))
            files = ["Add new trip"] + files
            self.trip_choices = files
            sublime.active_window().show_quick_panel(self.trip_choices, self.trip_choice_handler)

    def trip_choice_handler(self, index):
        if index == -1:
            return
        if index == 0:
            sublime.active_window().show_input_panel("New Trip name: ", "", self.add_new_trip, None, None)
        else:
            self.open_trip_file(self.trip_choices[index])

    def add_new_trip(self, trip_name):
        self.open_trip_file(trip_name)

    def open_trip_file(self, trip_name):
        file_path = os.path.join(self.trip_files_location, trip_name + ".yaml")
        newfile = False
        if not os.path.exists(file_path):
            newfile = True
        self.trip_file_view = sublime.active_window().open_file(file_path)

        def f():
            if self.trip_file_view.is_loading():
                sublime.set_timeout(f, 100)
            e = self.trip_file_view.begin_edit()
            self.trip_file_view.insert(e, 0, sampleLine[1:] + trip_name + ":\n\t")
            self.trip_file_view.end_edit(e)
        if newfile:
            sublime.set_timeout(f, 100)

    def on_pre_save(self, view):
        self.init()
        loc = view.file_name().find(self.trip_files_location)
        if loc != 0:
            return
        if view.file_name()[-5:] != ".yaml":
            return
        content = view.substr(sublime.Region(0, view.size()))
        content = content.replace("\t", " ")
        try:
            _trip = Trip.from_yaml(content)
            summary, soln = _trip.solve()
            self.add_result(view, self.format_result("\n".join(["Summary:"] + list("    " + line for line in summary.split("\n")) + ["", "", "Solution:"] + list("    " + line for line in soln.split("\n")))))
        except Exception as e:
            self.add_result(view, self.format_result(repr(e)))

    def format_result(self, result):
        lines = result.split("\n")
        max_len = max(len(line) for line in lines)
        max_len = max(max_len, 20)
        max_len *= 2
        formatted_result_array = ["#" * (max_len)] + ["#" + ' ' * (max_len - 2) + '#'] * 2
        for line in lines:
            cur_line = "#" + ' ' * 4 + line
            cur_line += ' ' * (max_len - len(cur_line) - 1)
            cur_line += '#'
            formatted_result_array.append(cur_line)
        formatted_result_array += ["#" + ' ' * (max_len - 2) + '#'] * 2
        formatted_result_array.append('#' * max_len)
        formatted_result_array.append('')
        return "\n".join(formatted_result_array)

    def add_result(self, view, result):
        lines = view.lines(sublime.Region(0, view.size()))
        state = 0
        ll = -1
        for l in xrange(len(lines) - 1, -1, -1):
            if state == 2:
                ll = l + 2
                break
            if state == 0:
                if len(view.substr(lines[l]).strip()) == 0:  # Stay in this state
                    continue
                if view.substr(lines[l].begin()) == "#":  # Comment
                    state = 1
                else:
                    state = 2
            elif state == 1:
                if lines[l].empty() or view.substr(lines[l].begin()) != "#":
                    state = 2

        # ll is the line from which to the end I have to use as the output space.
        print ll
        if ll < len(lines):
            reg = sublime.Region(lines[ll].begin(), view.size())
            edit = view.begin_edit()
            view.replace(edit, reg, "")
            view.end_edit(edit)
        edit = view.begin_edit()
        view.insert(edit, view.size(), result)
        view.end_edit(edit)
