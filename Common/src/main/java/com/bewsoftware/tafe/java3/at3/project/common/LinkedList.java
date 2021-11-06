/*
 *  File Name:    LinkedList.java
 *  Project Name: Java3AT2Q1
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ****************************************************************
 * Name: Bradley Willcott
 * ID:   M198449
 * Date: 22 July 2021
 * ****************************************************************
 */
package com.bewsoftware.tafe.java3.at3.project.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.Objects;

/**
 * This is an implementation of a Doubly Linked List.
 * <p>
 * <b>Note:</b>
 * <ul>
 * <li><i>This implementation does not allow {@code null } items to be
 * added.</i></li>
 * <li>By default, this implementation allows duplicate items to be added.<br>
 * However, it is possible to change that at instantiation - see: {@linkplain #LinkedList(boolean)
 * }.</li>
 * </ul>
 *
 *
 * @param <E> the type of elements held in this list
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class LinkedList<E> implements Externalizable, Iterable<E>
{

    /**
     * Error message text.
     */
    private static final String DATA_ITEGRITY_FAILED = "data integrity failed.";

    /**
     * Error message to go with {@link NullPointerException}
     */
    private static final String NO_NULL = "This implementation does not allow {@code null} items to be added.";

    /**
     * Used by serialization process.
     */
    private static final long serialVersionUID = 4263265852224464064L;

    /**
     * Are duplicate items allowed in this instance of {@linkplain LinkedList }?
     * <p>
     * @note Once set at instantiation, it cannot be changed.
     */
    private boolean allowDuplicates;

    /**
     * The node that was last retrieved by one of the traversal methods.
     */
    private transient Node<E> current;

    /**
     * The first node in the chain.
     */
    private transient Node<E> first;

    /**
     * The last node in the chain.
     */
    private transient Node<E> last;

    /**
     * The number of nodes/items in the chain.
     */
    private transient int size = 0;

    /**
     * Instantiate an empty default list object.
     * <p>
     * Duplicates are allowed.
     */
    public LinkedList()
    {
        allowDuplicates = true;
    }

    /**
     * Instantiate an empty list object.
     *
     * @param allowDuplicates Allow duplicates?
     */
    public LinkedList(final boolean allowDuplicates)
    {
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * Add a new item to the bottom of the list.
     * <p>
     * This item becomes the current reference point in the list.
     * <p>
     * <b>Note:</b> This implementation does not allow {@code null } items to be
     * added.
     *
     * @param item to add
     *
     * @return {@code true } if successful, {@code false } otherwise.
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean add(final E item)
    {
        Node<E> node = new Node<>(Objects.requireNonNull(item, NO_NULL));

        // Is the list empty?
        if (last == null)
        {
            // Yes - add first node
            first = node;
            last = node;
            current = node;
        } else
        {
            // Check for disallowed duplicates
            if (foundDisallowedDuplicate(item))
            {
                return false;
            }

            // Add node to tail of chain
            node.previous = last;
            last.next = node;
            last = node;
        }

        current = node;
        size++;
        return true;
    }

    /**
     * Reset this list to an empty list.
     */
    public void clear()
    {
        current = null;
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Searches for the first occurrence of the item in the list.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @param item to search for
     *
     * @return {@code true } if found, {@code false } otherwise
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean contains(final E item)
    {
        Objects.requireNonNull(item, NO_NULL);

        boolean rtn = false;
        current = first;

        // While we have something to work with
        while (current != null)
        {
            // Have we found one?
            if (current.item.equals(item))
            { // Yes - return true
                rtn = true;
                break;
            } else
            { // No - pointer to the next node
                current = current.next;
            }
        }

        return rtn;
    }

    /**
     * Retrieves the first item in the list.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @return the item at the top of the list, or {@code null } if the list is
     *         empty
     */
    public E first()
    {
        current = first;
        return (first != null) ? first.item : null;
    }

    /**
     * Searches for the first occurrence of the item in the list and returns it.
     * <p>
     * If found, this becomes the current reference point in the list.
     * <p>
     * <b>Note:</b> This is only useful, if the class used {@literal (<E>) } has
     * an {@code equals(Object) } method
     * that only checks for a subset of the classes properties. For example, a
     * Country
     * class that includes a list (internally) of cities. If only the country's
     * {@code name}
     * property is checked for equality, then using a new Country instance with
     * the required
     * name set, would work, returning the internal copy with all the
     * cities.
     *
     * @param item to search for
     *
     * @return {@code true } if found, {@code false } otherwise
     *
     * @throws NullPointerException if item is {@code null}
     */
    public E get(final E item)
    {
        if (contains(item))
        {
            return current.item;
        } else
        {
            return null;
        }
    }

    /**
     * Returns {@code true } if there is other item following the current one,
     * {@code false } otherwise.
     *
     * @return {@code true } if there is other item following the current one,
     *         {@code false } otherwise
     */
    public boolean hasNext()
    {
        return (current != null && current.next != null);
    }

    /**
     * Inserts the item before the current reference point.
     * <p>
     * If the current reference is not within the list, then the item
     * will be pushed onto the top of the list.
     *
     * @param item to be inserted
     *
     * @return {@code true } if successful, {@code false } otherwise.
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean insert(final E item)
    {

        // Are we out of bounds or pointing at the top?
        if (current == null || current.previous == null)
        { // Yes
            return push(item);

        } else
        { // No - so we insert it before the current node
            Node<E> node = new Node<>(Objects.requireNonNull(item, NO_NULL));

            // Check for disallowed duplicates
            if (foundDisallowedDuplicate(item))
            {
                return false;
            }

            // Add node
            // point node to previous node
            node.previous = current.previous;
            // point current node to node
            current.previous = node;

            // point previous node to node
            node.previous.next = node;
            // point node to next node
            node.next = current;

            // point current reference pointer to node
            current = node;
            size++;
            return true;
        }
    }

    /**
     * Inserts the item after the current reference point.
     * <p>
     * If the current reference is not within the list, then the item
     * will be appended onto the bottom of the list.
     *
     * @param item to be inserted
     *
     * @return {@code true } if successful, {@code false } otherwise.
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean insertAfter(final E item)
    {

        // Are we out of bounds or pointing at the top?
        if (current == null || current.next == null)
        { // Yes
            return add(item);

        } else
        { // No - so we insert it before the current node
            Node<E> node = new Node<>(Objects.requireNonNull(item, NO_NULL));

            // Check for disallowed duplicates
            if (foundDisallowedDuplicate(item))
            {
                return false;
            }

            // Add node
            // point node to next node
            node.next = current.next;
            // point current node to node
            current.next = node;

            // point next node to node
            node.next.previous = node;
            // point node to previous node
            node.previous = current;

            // point current reference pointer to node
            current = node;
            size++;
            return true;
        }
    }

    /**
     * Are duplicate items allowed in this instance of {@linkplain LinkedList }?
     *
     * @note Once set at instantiation, it cannot be changed.
     * @return the allowDuplicates
     */
    public boolean isAllowDuplicates()
    {
        return allowDuplicates;
    }

    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {

            /**
             * First time this instance of {@code hasNaext() } was run.
             */
            private boolean firstTime = true;

            @Override
            public boolean hasNext()
            {

                if (firstTime)
                {
                    return (size() > 0);

                } else
                {
                    return LinkedList.this.hasNext();
                }
            }

            @Override
            public E next()
            {
                if (firstTime)
                {
                    firstTime = false;
                    return first();
                } else
                {
                    return LinkedList.this.next();
                }
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    /**
     * Retrieves the last item in the list.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @return the item at the top of the list, or {@code null } if the list is
     *         empty
     */
    public E last()
    {
        current = last;
        return (last != null) ? last.item : null;
    }

    /**
     * Searches for the next occurrence of the item in the list.
     * <p>
     * The search starts at the current reference point, plus one.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @param item to search for.
     *
     * @return {@code true } if found, {@code false } otherwise
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean next(final E item)
    {
        Objects.requireNonNull(item, NO_NULL);

        boolean rtn = false;

        // While we have something to work with
        while (current != null)
        {
            // Have we found one?
            if (current.item.equals(item))
            { // Yes - return true
                rtn = true;
                break;
            } else
            { // No - pointer to the next node
                current = current.next;
            }
        }

        return rtn;
    }

    /**
     * Retrieves the next item from the list.
     * <p>
     * The item retrieved is the one following the last item retrieved
     * or located by this or other traversal methods.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @return the item found, or {@code null } if either past the end of the
     *         list, or the list is empty.
     */
    public E next()
    {
        E rtn = null;

        // Are we pointing to something?
        if (current != null)
        { // Yes - shift the pointer
            current = current.next;

            // Are we pointing to something now?
            if (current != null)
            { // Yes - get item to return
                rtn = current.item;
            }
        }

        return rtn;
    }

    /**
     * Removes and returns the first item of this list.
     * <p>
     * If successful, the new first item becomes the current reference point in
     * the list.
     *
     * @return the item at the top of the list, or {@code null } if the list is
     *         empty
     */
    public E pop()
    {
        E item = null;

        // Is the list empty?
        if (first != null)
        {
            // No - get item and remove node
            item = first.item;
            first = first.next;

            if (first != null)
            {
                first.previous = null;
            } else
            {
                last = null;
            }

            current = first;
            size--;
        } // Yes - nothing to do

        return item;
    }

    /**
     * Retrieves the previous item from the list.
     * <p>
     * The item retrieved is the one following the last item retrieved
     * or located by this or other traversal methods.
     * <p>
     * If found, this becomes the current reference point in the list.
     *
     * @return the item found, or {@code null } if either past the end of the
     *         list, or the list is empty.
     */
    public E prev()
    {
        E rtn = null;

        // Are we pointing to something?
        if (current != null)
        { // Yes - shift the pointer
            current = current.previous;

            // Are we pointing to something now?
            if (current != null)
            { // Yes - get item to return
                rtn = current.item;
            }
        }

        return rtn;
    }

    /**
     * Removes and returns the last item of this list.
     * <p>
     * If successful, the new last item becomes the current reference point in
     * the list.
     *
     * @return the item at the bottom of the list, or {@code null } if the list
     *         is empty
     */
    public E pull()
    {
        E item = null;

        // Is the list empty?
        if (last != null)
        {
            // No - get item and remove node
            item = last.item;
            last = last.next;

            if (last != null)
            {
                last.next = null;
            } else
            {
                first = null;
            }

            current = last;
            size--;
        } // Yes - nothing to do

        return item;
    }

    /**
     * Add a new item to the top of the list.
     * <p>
     * If successful, this item becomes the current reference point in the list.
     * <p>
     * <b>Note:</b> This implementation does not allow {@code null } items to be
     * added.
     *
     * @param item to add.
     *
     * @return {@code true } if successful, {@code false } otherwise.
     *
     * @throws NullPointerException if item is {@code null}
     */
    public boolean push(final E item)
    {
        Node<E> node = new Node<>(Objects.requireNonNull(item, NO_NULL));

        // Is the list empty?
        if (first == null)
        {
            // Yes - add first node
            first = node;
            last = node;
            current = node;
        } else
        {
            // Check for disallowed duplicates
            if (foundDisallowedDuplicate(item))
            {
                return false;
            }

            // Add node to tail of chain
            node.next = first;
            first.previous = node;
            first = node;
        }

        current = node;
        size++;
        return true;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int hashCode;
        int totalHashCode;

        // process 'allowDuplicates'
        allowDuplicates = in.readBoolean();
        hashCode = in.readInt();
        totalHashCode = hashCode;

        if (Objects.hashCode(allowDuplicates) != hashCode)
        {
            throw new IOException(DATA_ITEGRITY_FAILED);
        }

        // process 'Nodes'
        int numOfNodes = in.readInt();
        hashCode = in.readInt();
        totalHashCode += hashCode;

        if (Objects.hashCode(numOfNodes) != hashCode)
        {
            throw new IOException(DATA_ITEGRITY_FAILED);
        }

        for (int i = 0; i < numOfNodes; i++)
        {
            // process 'item'
            @SuppressWarnings("unchecked")
            E item = (E) in.readObject();
            hashCode = in.readInt();
            totalHashCode += hashCode;

            if (item.hashCode() != hashCode)
            {
                throw new IOException(DATA_ITEGRITY_FAILED);
            }

            add(item);
        }

        // process 'finish'
        int thc = in.readInt();

        if (totalHashCode != thc)
        {
            throw new IOException(DATA_ITEGRITY_FAILED);
        }
    }

    /**
     * Removes the item at the current reference point in the list.
     *
     * @return {@code true } if successful, {@code false } otherwise
     */
    public E remove()
    {
        E rtn = null;

        if (current != null)
        {
            // Are we at the top of the list?
            if (current.previous == null)
            { // Yes
                rtn = pop();
                // Are we at the bottom of the list?
            } else if (current.next == null)
            { // Yes
                rtn = pull();

            } else
            {
                // We are in the midst of the list
                current.previous.next = current.next;
                current.next.previous = current.previous;
                rtn = current.item;
                current = current.next;
                size--;
            }
        }

        return rtn;
    }

    /**
     * Returns the number of items in this list.
     *
     * @return the number of items in this list
     */
    public int size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("LinkedList{\n");

        sb.append("    size = ").append(size).append('\n');

        // process first item in list
        E item = first();

        if (item != null)
        {
            sb.append("    item = ").append(item).append('\n');
        }

        // process the rest of the list
        while (hasNext())
        {
            item = next();
            sb.append("    item = ").append(item).append('\n');
        }

        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        int hashCode;
        int totalHashCode;

        // process 'allowDuplicates'
        out.writeBoolean(allowDuplicates);
        hashCode = Objects.hashCode(allowDuplicates);
        totalHashCode = hashCode;
        out.writeInt(hashCode);

        // process 'Nodes'
        int numOfNodes = size;
        out.writeInt(numOfNodes);
        hashCode = Objects.hashCode(numOfNodes);
        totalHashCode += hashCode;
        out.writeInt(hashCode);

        // process first item in list
        E item = first();

        if (item != null)
        {
            out.writeObject(item);
            hashCode = item.hashCode();
            totalHashCode += hashCode;
            out.writeInt(hashCode);
        }

        // process the rest of the list
        while (hasNext())
        {
            E next = next();
            out.writeObject(next);
            hashCode = next.hashCode();
            totalHashCode += hashCode;
            out.writeInt(hashCode);
        }

        // finish
        out.writeInt(totalHashCode);
    }

    /**
     * Check for a duplicate record, if duplicates are not allowed.
     *
     * @param item to check for
     *
     * @return {@code true } if {@code allowDuplicates } is {@code false } and
     *         item is found,
     *         {@code false } otherwise.
     */
    private boolean foundDisallowedDuplicate(final E item)
    {
        boolean rtn = false;

        // Are duplicates allowed?
        if (!allowDuplicates)
        { // No - store current pointer
            Node<E> tempNode = current;

            // Does this item exist in the list?
            if (contains(item))
            { // Yes
                rtn = true;
            }

            // No - reset current pointer
            current = tempNode;
        }

        return rtn;
    }

    /**
     * This class is used by the {@linkplain LinkedList LinkedList&lt;E&gt;}
     * class to store the items,
     * and then be linked together into a chain.
     * <p>
     * This class is a struct alternative.
     *
     * @param <E> the type of element held in this node
     *
     * @since 1.0
     * @version 1.0
     */
    private class Node<E>
    {

        /**
         * The item being stored.
         */
        public final E item;

        /**
         * Link to the next node.
         */
        public Node<E> next;

        /**
         * Link to the previous node.
         */
        public Node<E> previous;

        /**
         * Instantiate a new Node object.
         *
         * @param item the object being stored
         */
        public Node(final E item)
        {
            this.item = item;
        }

        @Override
        public String toString()
        {
            return "Node{" + "item=" + item + '}';
        }

    }

}
