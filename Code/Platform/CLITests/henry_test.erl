-module(henry).
-compile(export_all).
-include_lib("eunit/include/eunit.hrl").

status() ->
  os:cmd('henry status').

help() ->
  os:cmd('henry help').

contains(Haystack,Needle) ->
  string:str(Haystack,Needle) >= 1.

status_test_() ->
  ?_test(
    [ ?_assert(contains(status(),"This is not a Git repository"))
    ]).

help_test_() ->
  ?_test(
    [ ?_assert(contains(help(),"Henry can only be used within a Git repository"))
    , ?_assert(contains(help(),"Henry's connection information can be displayed"))
    ]).
