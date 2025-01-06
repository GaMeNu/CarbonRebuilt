# Planned Features and Tasks for CarbonDF Library

### Planned
| **Feature**                       | **Status**                                         |
|-----------------------------------|----------------------------------------------------|
| DiamondFire code values           | DONE                                               |
| Variables, parameters             | DONE                                               |
| Type System                       | Variables, Parameters, Builtins returnValues       |
| Code blocks                       | Mostly Done<br/>(need to add support for dynamics) |
| Code actions                      | Mostly Done<br/>(dynamics)                         |
| Block Tags                        | DONE                                               |
| Return system                     | Support for PARAMETERS only                        |
| SubAction blocks                  | DONE                                               |
| Global token center (vars, funcs) | DONE                                               |
| Params + Rets for Funcs           |                                                    |
| Apply Type System to funcCalls    |                                                    |
| Templates                         | DONE (hopefully?)                                  |

### Ideas
| **Feature**                                  | **Status** | **Notes**                                                                                                                                                                                                                                                                                           |
|----------------------------------------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Functions as values / Higher order functions |            | An abstraction on %strings.<br/>This will include creating a new fake type, FUNCTION, that is a String behind the back. Using functions as values will use their names as "pointers" to the functions, and invoking them can be done with `CallFunc::"%var()"`<br/>Perhaps limited only to CarbonLang | 