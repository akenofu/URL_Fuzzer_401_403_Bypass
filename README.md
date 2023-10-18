# URL Fuzzer 401/403 Bypass

Fuzz the URL with all available ASCII characters to identify parser inconsistencies. Based on the work of Rafael da Costa Santos (https://rafa.hashnode.dev/exploiting-http-parsers-inconsistencies) and Orange Tsai (https://youtu.be/28xWcRegncw?si=t6BcScfQsz-2wFap).

## Demo

https://github.com/akenofu/URL_Fuzzer_403_Bypass/assets/44447755/611dc8bb-e538-4984-b91d-029f353d33b7

## How it works 
Inserts all ASCII characters (0-255) at pre-defined insertion points in the URL.
For the path `/admin/dashboard`, the following transformations are done:
- `0x85/admin/dashboard`
- `/0x85/admin0x85/dashboard`
- `/admin0x85/dashboard`
- `/admin/dashboard0x85`
- `/admin/dashboard/0x85/`

And, more...
