### Antlr v4 GenProject Model for the Antlr4 project generator

(c) 2012-2014 Gerald Rosenberg, Certiv Analytics

#### License
BSD/EPL License

#### Description 

Antlr GenProject provides a command line wizard for generating 
an Antlr4 project framework with a fairly standard form.  The 
directory structure is compatible with standard Eclipse/Java 
projects. 

The generated project is produced by specialization of an included
set of ST templates.  Specialization is data driven based on a simple
to edit Json configuration file and a standard Antlr generated parser
source file.

GenProject includes ReGen.  ReGen is capable of regenerating the GenProject
template files from a mostly otherwise ordinary model Antlr v4 project.
ReGen can also rebuild the configuration files used by GenProject to drive
the generation of a new specialized project.

This model project is the current minimal complete project used for the 
generation of the current set of GenProject templates.

