#!/bin/bash

##################### string #####################
to_upper() {
  echo $1 | tr '[a-z]' '[A-Z]'
}

to_lower() {
  echo $1 | tr '[A-Z]' '[a-z]'
}

##################### string end #####################
##################### bash params #####################
_param_variant() {
  for param in $1 ; do
    local variants=${param//\|/ }
    for variant in $variants ; do
      if [[ "$variant" = "$2" ]] ; then
        # Update the key to match the long version
        local arr=(${param//\|/ })
        let last=${#arr[@]}-1
        key="${arr[$last]}"
        return 0
      fi
    done
  done
  return 1
}

# Get input parameters in short or long notation, with no dependencies beyond bash
# usage:
#     # First, set your defaults
#     param_help=false
#     param_path="."
#     param_file=false
#     param_image=false
#     param_image_lossy=true
#     # Define allowed parameters
#     allowed_params="h|?|help p|path f|file i|image image-lossy"
#     # Get parameters from the arguments provided
#     _get_params $*
#
# Parameters will be converted into safe variable names like:
#     param_help,
#     param_path,
#     param_file,
#     param_image,
#     param_image_lossy
#
# Parameters without a value like "-h" or "--help" will be treated as
# boolean, and will be set as param_help=true
#
# Parameters can accept values in the various typical ways:
#     -i "path/goes/here"
#     --image "path/goes/here"
#     --image="path/goes/here"
#     --image=path/goes/here
# These would all result in effectively the same thing:
#     param_image="path/goes/here"
#
# Concatinated short parameters (boolean) are also supported
#     -vhm is the same as -v -h -m
get_shell_params() {

  local param_pair
  local key
  local value
  local shift_count

  while : ; do
    # Ensure we have a valid param. Allows this to work even in -u mode.
    if [[ $# == 0 || -z $1 ]] ; then
      break
    fi

    # Split the argument if it contains "="
    param_pair=(${1//=/ })
    # Remove preceeding dashes
    key="${param_pair[0]#--}"

    # Check for concatinated boolean short parameters.
    local nodash="${key#-}"
    local breakout=false
    if [[ "$nodash" != "$key" && ${#nodash} -gt 1 ]]; then
      # Extrapolate multiple boolean keys in single dash notation. ie. "-vmh" should translate to: "-v -m -h"
      local short_param_count=${#nodash}
      let new_arg_count=$#+$short_param_count-1
      local new_args=""
      # $str_pos is the current position in the short param string $nodash
      for (( str_pos=0; str_pos<new_arg_count; str_pos++ )); do
        # The first character becomes the current key
        if [ $str_pos -eq 0 ] ; then
          key="${nodash:$str_pos:1}"
          breakout=true
        fi
        # $arg_pos is the current position in the constructed arguments list
        let arg_pos=$str_pos+1
        if [ $arg_pos -gt $short_param_count ] ; then
          # handle other arguments
          let orignal_arg_number=$arg_pos-$short_param_count+1
          local new_arg="${!orignal_arg_number}"
        else
          # break out our one argument into new ones
          local new_arg="-${nodash:$str_pos:1}"
        fi
        new_args="$new_args \"$new_arg\""
      done
      # remove the preceding space and set the new arguments
      eval set -- "${new_args# }"
    fi
    if ! $breakout ; then
      key="$nodash"
    fi

    # By default we expect to shift one argument at a time
    shift_count=1
    if [ "${#param_pair[@]}" -gt "1" ] ; then
      # This is a param with equals notation
      value="${param_pair[1]}"
    else
      # This is either a boolean param and there is no value,
      # or the value is the next command line argument
      # Assume the value is a boolean true, unless the next argument is found to be a value.
      value=true
      if [[ $# -gt 1 && -n "$2" ]]; then
        local nodash="${2#-}"
        if [ "$nodash" = "$2" ]; then
          # The next argument has NO preceding dash so it is a value
          value="$2"
          shift_count=2
        fi
      fi
    fi
    # Check that the param being passed is one of the allowed params
    if _param_variant "$allowed_params" "$key" ; then
      # --key-name will now become param_key_name
      eval $(to_upper param_${key//-/_})="$value"
    else
      printf 'WARNING: Unknown option (ignored): %s\n' "$1" >&2
    fi
    shift $shift_count
  done
}
##################### bash params end #####################