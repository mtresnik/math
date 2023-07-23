package com.resnik.math.symbo.logic.operation

import com.resnik.math.symbo.logic.operation.base.And
import com.resnik.math.symbo.logic.operation.base.Iff
import com.resnik.math.symbo.logic.operation.base.Implies
import com.resnik.math.symbo.logic.operation.base.Or

infix fun LogicalOperation.or(other: LogicalOperation) : Or {
    return Or(this, other)
}

infix fun LogicalOperation.or(other: Boolean) : Or {
    return Or(this, LogicalConstant(other))
}

infix fun Boolean.or(other: LogicalOperation) : Or {
    return Or(LogicalConstant(this), other)
}

infix fun LogicalOperation.and(other: LogicalOperation) : And {
    return And(this, other)
}

infix fun LogicalOperation.and(other: Boolean) : And {
    return And(this, LogicalConstant(other))
}

infix fun Boolean.and(other: LogicalOperation) : And {
    return And(LogicalConstant(this), other)
}

infix fun LogicalOperation.implies(other: LogicalOperation) : Implies {
    return Implies(this, other)
}

infix fun Boolean.implies(other: Boolean): Boolean {
    return !this || other
}

infix fun LogicalOperation.implies(other: Boolean): Implies {
    return Implies(this, LogicalConstant(other))
}

infix fun Boolean.implies(other: LogicalOperation): Implies {
    return Implies(LogicalConstant(this), other)
}

infix fun LogicalOperation.iff(other: Boolean): Iff {
    return Iff(this, LogicalConstant(other))
}

infix fun Boolean.iff(other: LogicalOperation): Iff {
    return Iff(LogicalConstant(this), other)
}

infix fun LogicalOperation.iff(other: LogicalOperation) : Iff {
    return Iff(this, other)
}

infix fun Boolean.iff(other: Boolean): Boolean {
    return (!this || other) && (this || !other)
}