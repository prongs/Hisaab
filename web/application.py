import os
from mako import exceptions
from mako.lookup import TemplateLookup
import tornado.ioloop
import tornado.web
from tornado import httpclient
from utils import *
import dbhelper
import json
import solver
root = os.path.dirname(__file__)
template_root = os.path.join(root, "templates")
blacklist_templates = ('layouts',)

template_lookup = TemplateLookup(input_encoding='utf-8',
                                 output_encoding='utf-8',
                                 encoding_errors='replace',
                                 directories=[template_root])


db = dbhelper.DBHelper()


def render_template(filename):
    if os.path.isdir(os.path.join(template_root, filename)):
        filename = os.path.join(filename, 'index.html')
    else:
        filename = "%s.html" % filename
    if any(filename.lstrip('/').startswith(p) for p in blacklist_templates):
        raise httpclient.HTTPError(404)
    try:
        return template_lookup.get_template(filename).render()
    except exceptions.TopLevelLookupException:
        raise httpclient.HTTPError(404)


class DefaultHandler(tornado.web.RequestHandler):
    def get_error_html(self, status_code, exception, **kwargs):
        if hasattr(exception, 'code'):
            self.set_status(exception.code)
            if exception.code == 500:
                return exceptions.html_error_template().render()
            return render_template(str(exception.code))
        return exceptions.html_error_template().render()


class MainHandler(DefaultHandler):
    def get(self, filename):
        self.write(render_template(filename))


class RESTHandler(DefaultHandler):
    def get(self, table, _id):
        result = db.select(table) if not _id else db.select(table, _id=_id)[0]
        self.write(json.dumps(result))

application = tornado.web.Application([
    (r'^/REST/(.*?)/(.*)$', RESTHandler),
    (r'^/(.*)$', MainHandler),
    ], debug=True, static_path=os.path.join(root, 'static'))

if __name__ == '__main__':
    application.listen(20000)
    tornado.ioloop.IOLoop.instance().start()

