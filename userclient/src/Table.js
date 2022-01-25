import axios from "axios";
import React, { useState, useEffect, useMemo, useRef } from "react";
import Pagination from "@material-ui/lab/Pagination";
import { useTable } from 'react-table'

export default function Table({ columns}) {
  const [filterLastName, setFilterLastName] = useState("");
  const [filterAge, setFilterAge] = useState(0);
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [count, setCount] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageSort, setPageSort] = useState("id,ASC");
  const dataRef = useRef();
  const pageSizes = [10, 20, 50];
  const sortArray = [{key:"lastName,ASC",  value:"Lastname Ascending"}, {key:"lastName,DESC",  value:"Lastname Descending"}, {key:"age,ASC",  value:"Age Low to High"},{key:"age,DESC",  value:"Age High to Low"}]
  // Use the state and functions returned from useTable to build your UI
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    state: { pageIndex },
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0, pageSize: 10 },
    }
  )
  dataRef.current = data;
  const onChangeLastName = (e) => {
    setFilterLastName(e.target.value);
  };
  const onChangeAge = (e) => {
    setFilterAge(e.target.value);
  };

  const filterTable = () =>{
    setPage(1);
    retrieveUserData();
  }
  const handlePageChange = (event, value) => {
    setPage(value);
  };
  const handlePageSizeChange = (event) => {
    setPageSize(event.target.value);
    setPage(1);
  };
  const handlePageSortChange = (event) => {
    setPageSort(event.target.value);
    console.log("Sorting changed", event.target.value);
    setPage(1);
  };
  const getRequestParams = (lastName, age, page, pageSize, pageSort) => {
    let params = {};

    if (lastName) {
      params["lastName"] = lastName;
    }
    if (age) {
      params["age"] = age;
    }
    if (page) {
      params["page"] = page - 1;
    }

    if (pageSize) {
      params["size"] = pageSize;
    }

    if(pageSort){
      params["sort"] = pageSort;
      if(pageSort.includes("id") && page!=1 && !(lastName || age)){
        params["offset"] = data[data.length-1].id;
      }
    }else{
      params["sort"] ='id,ASC'; //default sort
    }
    return params;
  };

  const retrieveUserData = () => {
    const params = getRequestParams(filterLastName, filterAge, page, pageSize, pageSort);

    axios.get('http://localhost:8080/api/users',{params})
      .then((response) => {
        const data = response.data;
        const totalCount = response.headers['X-Total-Count'];

        setData(data);
        setCount(Math.ceil(totalCount/pageSize));

        console.log(response.data);
      })
      .catch((e) => {
        console.log(e);
      });
  };


  useEffect(retrieveUserData, [page, pageSize, pageSort]);

  const reloadTable = () => {
    retrieveUserData();
  };
  // Render the UI for your table
  return (
    <>
    <div className="filterform">
          <input
            type="text"
            placeholder="Last Name"
            value={filterLastName}
            onChange={onChangeLastName}
          />
          <input
            type="number"
            placeholder="Age"
            onChange={onChangeAge}
          />
          <div >
            <button
              type="button"
              onClick={filterTable}
            >
              Search
            </button>
          </div>
      </div>

      <div className="pagination">
        <div >
          {"Items per Page: "}
          <select onChange={handlePageSizeChange} value={pageSize}>
            {pageSizes.map((size) => (
              <option key={size} value={size}>
                {size}
              </option>
            ))}
          </select>

          {          "Sort: "}
          <select onChange={handlePageSortChange} value={pageSort}>
            {sortArray.map((sort) => (
              <option key={sort.key} value={sort.key}>
                {sort.value}
              </option>
            ))}
          </select>

          <Pagination
            count={count}
            page={page}
            siblingCount={1}
            boundaryCount={1}
            variant="outlined"
            shape="rounded"
            onChange={handlePageChange}
          />
        </div>

      <table {...getTableProps()}>
        <thead>
          {headerGroups.map(headerGroup => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map(column => (
                <th {...column.getHeaderProps()}>{column.render('Header')}
                <span>
                    {column.isSorted
                      ? column.isSortedDesc
                        ? ' 🔽'
                        : ' 🔼'
                      : ''}
                  </span>
                  </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {rows.map((row, i) => {
            prepareRow(row)
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map(cell => {
                  return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                })}
              </tr>
            )
          })}
        </tbody>
      </table>
      
      </div>
    </>
  )
}