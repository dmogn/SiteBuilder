# SiteBuilder

## HTML static site generation. 

HTML text pages/images on input, structured HTML website on output. The general purpose is documentation publishing.


Requirements: JDK 5.0+

### Status: Stable


### Features:

- Fully static HTML output (no JS).

- Structured pages hierarchy, organized by directory structure.

- Page template and main page template.

- Only relative links generation (site can be copied anywere, browsed offline, distributed by archive, etc.)

- Keep original HTML page metadata (\<title\>, \<meta name="Keywords" content="..."\>, \<meta name="Description" content="..."\>, etc. )

- Multilanguage sites with page-to-page links between languages.


### Usage: 

java -jar sitebuilder-1.0.jar -c path_to/site.config

### Usage example:

java -jar target/sitebuilder-1.0.jar -c examples/siriusmg.ru/site.config


**site.config** example:

<pre><code>
SOURCE_DIR "examples/siriusmg.ru/"
DESTINATION_DIR "examples/siriusmg.ru.publish/"
INDEX_FILE_TEMPLATE "examples/siriusmg.ru/templates/index_template.html"
OTHER_FILES_TEMPLATE "examples/siriusmg.ru/templates/file_template.html"
</code></pre>

Pages list format in **index.config** (# - symbol of break line in page menu):

<pre><code>
page.html "Link to the page name"
subdirectory_name
#
subdirectory2_name/direct_included_page.html "Link text"
;subdirectory3/disabled_page.html  "Do not show this link to page"
</code></pre>
