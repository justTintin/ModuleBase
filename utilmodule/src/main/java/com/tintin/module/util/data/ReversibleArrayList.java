package com.tintin.module.util.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/2/24] 
*/
public class ReversibleArrayList<T> extends ArrayList<T>
{

    public ReversibleArrayList(Collection<T> c)
    {
        super(c);
    }

    public ReversibleArrayList()
    {
        super();
    }

    public Iterable<T> reversed()
    {
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                return new Iterator<T>()
                {
                    int current = size() - 1;

                    @Override
                    public boolean hasNext()
                    {
                        return current > -1;
                    }

                    @Override
                    public T next()
                    {
                        return get(current--);
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }

        };
    }

}