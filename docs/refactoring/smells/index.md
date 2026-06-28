# Code Smells Index

Dưới đây là danh sách đầy đủ các Code Smells theo phân loại của **Refactoring Guru**. Mỗi mục đều đã có tài liệu chi tiết đính kèm.

## Categories

### [Bloaters](bloaters.md)
Methods and classes that have increased to such gargantuan proportions that they are hard to work with.
- [Long Method](long-method.md)
- [Large Class](large-class.md)
- [Primitive Obsession](primitive-obsession.md)
- [Long Parameter List](long-parameter-list.md)
- [Data Clumps](data-clumps.md)

### [Object-Orientation Abusers](oo-abusers.md)
Incorrect or incomplete application of object-oriented programming principles.
- [Switch Statements](switch-statements.md)
- [Temporary Field](temporary-field.md)
- [Refused Bequest](refused-bequest.md)
- [Alternative Classes with Different Interfaces](alternative-classes-with-different-interfaces.md)

### [Change Preventers](change-preventers.md)
Smells that ensure if you need to change something in one place, you have to make many changes in other places too.
- [Divergent Change](divergent-change.md)
- [Parallel Inheritance Hierarchies](parallel-inheritance-hierarchies.md)
- [Shotgun Surgery](shotgun-surgery.md)

### [Dispensables](dispensables.md)
Pointless and unneeded code whose absence would make the code cleaner and more efficient.
- [Comments](comments.md)
- [Duplicate Code](duplicate-code.md)
- [Data Class](data-class.md)
- [Dead Code](dead-code.md)
- [Lazy Class](lazy-class.md)
- [Speculative Generality](speculative-generality.md)

### [Couplers](couplers.md)
All the smells in this group contribute to excessive coupling between classes.
- [Feature Envy](feature-envy.md)
- [Inappropriate Intimacy](inappropriate-intimacy.md)
- [Incomplete Library Class](incomplete-library-class.md)
- [Message Chains](message-chains.md)
- [Middle Man](middle-man.md)
