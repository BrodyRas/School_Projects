from PyQt5.QtCore import QLineF, QPointF, QObject


class Node:
    # Point w x/y coordinates
    point = QPointF()

    # Pointers to the previous and next points in circular list
    prev = None
    next = None
