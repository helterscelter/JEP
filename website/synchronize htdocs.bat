echo -- DRY RUN -------------------
call rsync -rvztn -e ssh htdocs/ nathanfunk@jep.sourceforge.net:/home/groups/j/je/jep/htdocs/
echo -- ACTUAL TRANSFER -------------------
pause
rem call rsync -rvzt -e ssh htdocs/ nathanfunk@jep.sourceforge.net:/home/groups/j/je/jep/htdocs/
pause