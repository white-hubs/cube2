# Safety Personnel Overview

The safety personnel app implements the first version of the safety production personnel management backend.

Main domains:

- Safety personnel pool based on OUAA users.
- Safety production committee management.
- Certificate category, type, and employee certificate management.
- Organization configuration for home page display.
- Safety resume aggregation across work-center and conference data sources.

Storage:

- Database: `cube_srv_gen_safety_personnel`.
- Table prefix: `sp_`.
- Logical delete only. Unique constraints include `deleted`.
